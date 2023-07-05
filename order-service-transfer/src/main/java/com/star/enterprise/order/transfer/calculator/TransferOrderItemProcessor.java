package com.star.enterprise.order.transfer.calculator;

import com.querydsl.jpa.impl.JPAQuery;
import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.service.OrderItemService;
import com.star.enterprise.order.refund.model.OrderItemDelegate;
import com.star.enterprise.order.transfer.data.es.entity.OrderTransferSearchInfoEntity;
import com.star.enterprise.order.transfer.data.es.entity.SearchedOrderTransferItemInfo;
import com.star.enterprise.order.transfer.data.jpa.OrderTransferItemFeeRepository;
import com.star.enterprise.order.transfer.data.jpa.OrderTransferItemInfoRepository;
import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferItemFeeEntity;
import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferItemInfoEntity;
import com.star.enterprise.order.transfer.data.jpa.entity.QOrderTransferItemFeeEntity;
import com.star.enterprise.order.transfer.data.jpa.entity.QOrderTransferItemInfoEntity;
import com.star.enterprise.order.transfer.handler.business.BusinessTypeTransferHandler;
import com.star.enterprise.order.transfer.model.OrderTransferInfo;
import com.star.enterprise.order.transfer.model.trans.OrderTransferItemFeeSummaryTransObject;
import com.star.enterprise.order.transfer.model.trans.OrderTransferItemSummaryTransObject;
import com.star.enterprise.order.transfer.model.trans.OrderTransferSummaryTransObject;
import com.star.enterprise.order.transfer.processor.DelegatingTransferPredicateProcessor;
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

import static com.star.enterprise.order.base.exception.RestCode.TRANSFER_ITEM_NOT_FOUND;
import static com.star.enterprise.order.base.exception.RestCode.TRANSFER_OPERATE_ERROR;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
@Component
@RequiredArgsConstructor
public class TransferOrderItemProcessor implements OrderTransferCalculator {

    private final ApplicationContext context;

    private final OrderItemService orderItemService;

    private final OrderTransferItemInfoRepository itemInfoRepository;

    private final OrderTransferItemFeeRepository itemFeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void preCalculate(TargetUser target, OrderTransferInfo transferInfo, TransferAccumulateHolder holder) {
        transferInfo.items().forEach(item -> {
            var object = this.orderItemService.getOrderItemTransObject(item.orderItemId()).orElseThrow(() -> new BusinessWarnException(TRANSFER_ITEM_NOT_FOUND, "error.transfer.notFound"));
            var chain = DelegatingTransferPredicateProcessor.build(this.context, BusinessTypeEnum.of(object.getBusinessType()));
            var delegate = new OrderItemDelegate(object);
            if (!chain.predicate(target, delegate)) {
                throw new BusinessWarnException(TRANSFER_ITEM_NOT_FOUND, "error.transfer.notAllow");
            }
            if (item.transferNumber().compareTo(delegate.getNumberLeft()) > 0) {
                throw new BusinessWarnException(TRANSFER_ITEM_NOT_FOUND, "error.transfer.notAllow");
            }
            if (item.transferApportion().compareTo(delegate.getApportionLeft()) > 0) {
                throw new BusinessWarnException(TRANSFER_ITEM_NOT_FOUND, "error.transfer.notAllow");
            }
            var handler = BusinessTypeTransferHandler.createHandler(BusinessTypeEnum.of(delegate.getBusinessType()), this.context);
            item.context().setHandler(handler);
            handler.processItemBusinessInfo(target, item, holder);
            item.context().setDelegate(delegate);
            var opt = item.operator();
            var singlePrice = delegate.getItemFee().getDueCollectSinglePrice();
            opt.setCurrentBalance(delegate.getNumberLeft().multiply(singlePrice));
            opt.addTransferNumberPrice(item.transferNumber().multiply(singlePrice));
            holder.addDueTransferPrice(opt.getTransferNumberPrice()).addDueTransferPrice(opt.getTransferApportionPrice());
        });
    }

    @Override
    public void postCalculate(String transferOrderId, TargetUser target, OrderTransferInfo transferInfo, TransferAccumulateHolder holder) {
        transferInfo.items().forEach(item -> {
            var delegate = item.context().getDelegate();
            var fee = delegate.getItemFee();
            var infoEntity = new OrderTransferItemInfoEntity().setOrderPaidReceiptNo(item.receiptNo()).setTransferOrderId(transferOrderId).setOrderId(item.orderId())
                    .setOrderItemId(item.orderItemId()).setBusinessId(item.businessId()).setBusinessType(item.businessType()).setBusinessName(delegate.getProductName())
                    .setSpecId(delegate.getSpecId()).setSpecName(delegate.getSpecName()).setPurchaseDate(item.purchasedDate()).setExtendInfo(delegate.getExtendInfo())
                    .setNumber(new BigDecimal(delegate.getNumber())).setApportion(new BigDecimal(delegate.getApportion())).setNumberLeft(delegate.getNumberLeft())
                    .setApportionLeft(delegate.getApportionLeft()).setOriginSinglePrice(fee.getOriginSinglePrice()).setOriginTotalPrice(fee.getOriginTotalPrice())
                    .setDueCollectSinglePrice(fee.getDueCollectSinglePrice()).setDueCollectPrice(fee.getDueCollectPrice()).setWebViewToast(item.webViewToast());
            var saved = this.itemInfoRepository.saveAndFlush(infoEntity);
            if (!StringUtils.hasText(saved.getTransferOrderItemId())) {
                throw new BusinessWarnException(TRANSFER_OPERATE_ERROR, "error.transfer.saveErr");
            }
            var opt = item.operator();
            var feeEntity = new OrderTransferItemFeeEntity().setTransferOrderId(transferOrderId).setTransferOrderItemId(saved.getTransferOrderItemId()).setOrderId(item.orderId())
                    .setOrderItemId(item.orderItemId()).setCurrentStandardPrice(delegate.getCurrentStandardPrice()).setCurrentBalance(opt.getCurrentBalance())
                    .setTransferNumber(item.transferNumber()).setTransferApportion(item.transferApportion()).setTransferNumberPrice(opt.getTransferNumberPrice())
                    .setTransferApportionPrice(opt.getTransferApportionPrice());
            this.itemFeeRepository.save(feeEntity);
            item.context().setRefundOrderItemId(saved.getTransferOrderItemId());
            item.context().getHandler().processItemAfterSaved(transferOrderId, target, item, holder);
        });
    }

    @Override
    public void preAsyncElastic(String transferOrderId, OrderTransferSearchInfoEntity entity, TargetUser target) {
        // 保存所有计算好的收费类型
        Function<OrderTransferItemInfoEntity, SearchedOrderTransferItemInfo> function = i ->
                new SearchedOrderTransferItemInfo(i.getOrderPaidReceiptNo(), i.getOrderId(), i.getOrderItemId(), i.getTransferOrderItemId(), i.getBusinessType(), i.getBusinessId(), i.getBusinessName(), i.getSpecName());
        var items = this.itemInfoRepository.findByTransferOrderId(transferOrderId).stream().map(function).toList();
        OrderTransferCalculator.super.preAsyncElastic(transferOrderId, entity.setItems(items), target);
    }

    @Override
    public void postConsumerElastic(Map<String, OrderTransferSummaryTransObject> objects) {
        var query = new JPAQuery<>(this.entityManager);
        var qoi = QOrderTransferItemInfoEntity.orderTransferItemInfoEntity;
        var qof = QOrderTransferItemFeeEntity.orderTransferItemFeeEntity;
        var tuple = query.select(qoi, qof).from(qoi).leftJoin(qof).on(qoi.transferOrderItemId.eq(qof.transferOrderItemId)).where(qoi.transferOrderId.in(objects.keySet())).fetch();
        var map = tuple.stream().map(t -> new OrderTransferItemSummaryTransObject(t.get(qoi), new OrderTransferItemFeeSummaryTransObject(t.get(qof)))).collect(Collectors.groupingBy(OrderTransferItemSummaryTransObject::getTransferOrderId));
        objects.keySet().stream().filter(map::containsKey).forEach(key -> objects.get(key).setItems(map.get(key)));
    }

}
