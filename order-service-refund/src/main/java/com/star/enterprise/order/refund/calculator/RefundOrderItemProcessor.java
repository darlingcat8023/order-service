package com.star.enterprise.order.refund.calculator;

import com.querydsl.jpa.impl.JPAQuery;
import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.service.OrderItemService;
import com.star.enterprise.order.refund.data.es.entity.OrderRefundSearchInfoEntity;
import com.star.enterprise.order.refund.data.es.entity.SearchedOrderRefundItemInfo;
import com.star.enterprise.order.refund.data.jpa.OrderRefundItemFeeRepository;
import com.star.enterprise.order.refund.data.jpa.OrderRefundItemInfoRepository;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundItemFeeEntity;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundItemInfoEntity;
import com.star.enterprise.order.refund.data.jpa.entity.QOrderRefundItemFeeEntity;
import com.star.enterprise.order.refund.data.jpa.entity.QOrderRefundItemInfoEntity;
import com.star.enterprise.order.refund.handler.business.BusinessTypeRefundHandler;
import com.star.enterprise.order.refund.model.OrderItemDelegate;
import com.star.enterprise.order.refund.model.OrderRefundInfo;
import com.star.enterprise.order.refund.model.trans.OrderRefundItemFeeSummaryTransObject;
import com.star.enterprise.order.refund.model.trans.OrderRefundItemSummaryTransObject;
import com.star.enterprise.order.refund.model.trans.OrderRefundSummaryTransObject;
import com.star.enterprise.order.refund.processor.DelegatingPredicateProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.star.enterprise.order.base.exception.RestCode.REFUND_ITEM_NOT_FOUND;
import static com.star.enterprise.order.base.exception.RestCode.REFUND_OPERATE_ERROR;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
@Component
@RequiredArgsConstructor
public class RefundOrderItemProcessor implements OrderRefundCalculator {

    private final ApplicationContext context;

    private final OrderItemService itemService;

    private final OrderRefundItemInfoRepository itemInfoRepository;

    private final OrderRefundItemFeeRepository itemFeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void preCalculate(TargetUser target, OrderRefundInfo refundInfo, RefundAccumulateHolder holder) {
        refundInfo.items().forEach(item -> {
            var object = this.itemService.getOrderItemTransObject(item.orderItemId()).orElseThrow(() -> new BusinessWarnException(REFUND_ITEM_NOT_FOUND, "error.refund.notFound"));
            var chain = DelegatingPredicateProcessor.build(this.context, BusinessTypeEnum.of(object.getBusinessType()));
            var delegate = new OrderItemDelegate(object);
            var handler = BusinessTypeRefundHandler.createHandler(BusinessTypeEnum.of(delegate.getBusinessType()), this.context);
            item.context().setHandler(handler);
            item.context().setDelegate(delegate);
            handler.processItemBeforePredicate(target, delegate);
            if (!chain.predicate(target, delegate)) {
                throw new BusinessWarnException(REFUND_ITEM_NOT_FOUND, "error.refund.notAllow");
            }
            if (item.refundNumber().compareTo(delegate.getNumberLeft()) > 0) {
                throw new BusinessWarnException(REFUND_ITEM_NOT_FOUND, "error.refund.notAllow");
            }
            if (item.refundApportion().compareTo(delegate.getApportionLeft()) > 0) {
                throw new BusinessWarnException(REFUND_ITEM_NOT_FOUND, "error.refund.notAllow");
            }
            handler.processItemBusinessInfo(target, item, holder);
            var opt = item.operator();
            var singlePrice = delegate.getItemFee().getDueCollectSinglePrice();
            opt.setCurrentBalance(delegate.getNumberLeft().multiply(singlePrice));
            opt.addRefundNumberPrice(item.refundNumber().multiply(singlePrice));
            holder.addDueRefundPrice(opt.getRefundNumberPrice()).addDueRefundPrice(opt.getRefundApportionPrice());
        });
    }

    @Override
    public void postCalculate(String refundOrderId, TargetUser target, OrderRefundInfo refundInfo, RefundAccumulateHolder holder) {
        refundInfo.items().forEach(item -> {
            var delegate = item.context().getDelegate();
            var fee = delegate.getItemFee();
            var infoEntity = new OrderRefundItemInfoEntity().setOrderPaidReceiptNo(item.receiptNo()).setRefundOrderId(refundOrderId).setOrderId(item.orderId())
                    .setOrderItemId(item.orderItemId()).setBusinessId(item.businessId()).setBusinessType(item.businessType()).setBusinessName(delegate.getProductName())
                    .setSpecId(delegate.getSpecId()).setSpecName(delegate.getSpecName()).setPurchaseDate(item.purchasedDate()).setExtendInfo(delegate.getExtendInfo())
                    .setNumber(new BigDecimal(delegate.getNumber())).setApportion(new BigDecimal(delegate.getApportion())).setNumberLeft(delegate.getNumberLeft())
                    .setApportionLeft(delegate.getApportionLeft()).setOriginSinglePrice(fee.getOriginSinglePrice()).setOriginTotalPrice(fee.getOriginTotalPrice())
                    .setDueCollectSinglePrice(fee.getDueCollectSinglePrice()).setDueCollectPrice(fee.getDueCollectPrice()).setWebViewToast(item.webViewToast());
            var saved = this.itemInfoRepository.saveAndFlush(infoEntity);
            if (!StringUtils.hasText(saved.getRefundOrderItemId())) {
                throw new BusinessWarnException(REFUND_OPERATE_ERROR, "error.refund.saveErr");
            }
            var opt = item.operator();
            var feeEntity = new OrderRefundItemFeeEntity().setRefundOrderId(refundOrderId).setRefundOrderItemId(saved.getRefundOrderItemId()).setOrderId(item.orderId())
                    .setOrderItemId(item.orderItemId()).setCurrentStandardPrice(delegate.getCurrentStandardPrice()).setCurrentBalance(opt.getCurrentBalance())
                    .setRefundNumber(item.refundNumber()).setRefundApportion(item.refundApportion()).setRefundNumberPrice(opt.getRefundNumberPrice())
                    .setRefundApportionPrice(opt.getRefundApportionPrice()).setRefundOverPrice(opt.getRefundOverPrice());
            this.itemFeeRepository.save(feeEntity);
            item.context().setRefundOrderItemId(saved.getRefundOrderItemId());
            item.context().getHandler().processItemAfterSaved(refundOrderId, target, item, holder);
        });
    }

    @Override
    public void preAsyncElastic(String refundOrderId, OrderRefundSearchInfoEntity entity, TargetUser target) {
        // 保存所有计算好的收费类型
        Function<OrderRefundItemInfoEntity, SearchedOrderRefundItemInfo> function = i ->
                new SearchedOrderRefundItemInfo(i.getOrderPaidReceiptNo(), i.getOrderId(), i.getOrderItemId(), i.getRefundOrderItemId(), i.getBusinessType(), i.getBusinessId(), i.getBusinessName(), i.getSpecName());
        var items = this.itemInfoRepository.findByRefundOrderId(refundOrderId).stream().map(function).toList();
        OrderRefundCalculator.super.preAsyncElastic(refundOrderId, entity.setItems(items), target);
    }

    @Override
    public void postConsumerElastic(Map<String, OrderRefundSummaryTransObject> objects) {
        var query = new JPAQuery<>(this.entityManager);
        QOrderRefundItemInfoEntity qoi = QOrderRefundItemInfoEntity.orderRefundItemInfoEntity;
        QOrderRefundItemFeeEntity qof = QOrderRefundItemFeeEntity.orderRefundItemFeeEntity;
        var tuple = query.select(qoi, qof).from(qoi).leftJoin(qof).on(qoi.refundOrderItemId.eq(qof.refundOrderItemId)).where(qoi.refundOrderId.in(objects.keySet())).fetch();
        var map = tuple.stream().map(t -> new OrderRefundItemSummaryTransObject(t.get(qoi), new OrderRefundItemFeeSummaryTransObject(t.get(qof)))).collect(Collectors.groupingBy(OrderRefundItemSummaryTransObject::getRefundOrderId));
        objects.keySet().stream().filter(map::containsKey).forEach(key -> objects.get(key).setItems(map.get(key)));
    }

}
