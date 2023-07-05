package com.star.enterprise.order.core.calculator.processor;

import com.querydsl.jpa.impl.JPAQuery;
import com.star.enterprise.order.base.exception.BusinessErrorException;
import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.adapter.MatchResultDesAdapter;
import com.star.enterprise.order.core.calculator.CalculateProcessor;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.data.es.entity.SearchedOrderItemInfo;
import com.star.enterprise.order.core.data.jpa.OrderItemFeeRepository;
import com.star.enterprise.order.core.data.jpa.OrderItemInfoRepository;
import com.star.enterprise.order.core.data.jpa.entity.OrderItemFeeEntity;
import com.star.enterprise.order.core.data.jpa.entity.OrderItemInfoEntity;
import com.star.enterprise.order.core.data.jpa.entity.QOrderItemFeeEntity;
import com.star.enterprise.order.core.data.jpa.entity.QOrderItemInfoEntity;
import com.star.enterprise.order.core.handler.business.BusinessTypeHandler;
import com.star.enterprise.order.core.model.trans.OrderItemFeeSummaryTransObject;
import com.star.enterprise.order.core.model.trans.OrderItemSummaryTransObject;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.star.enterprise.order.base.exception.RestCode.CORE_OPERATE_ERROR;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * 用来保存订单项的信息
 * @author xiaowenrou
 * @date 2022/10/24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderItemSaveProcessor implements CalculateProcessor {

    @Override
    public int getOrder() {
        return 100;
    }

    private final ApplicationContext applicationContext;

    private final OrderItemInfoRepository itemInfoRepository;

    private final OrderItemFeeRepository itemFeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void preCalculate(OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        orderDetail.items().forEach(item -> {
            var handler = BusinessTypeHandler.createHandler(BusinessTypeEnum.of(item.businessType()), this.applicationContext);
            item.context().setHandler(handler);
            handler.processItemBusinessInfo(target, item, holder);
            holder.addOrderTotalPrice(item.operator().getOriginalTotalPrice());
            holder.addOrderAfterDiscountTotalPrice(item.operator().getAfterDiscountTotalPrice());
            holder.addOrderDueCollectPrice(item.operator().getAfterDiscountTotalPrice());
        });
        chain.preCalculate(orderDetail, chain, holder, target);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void postCalculate(String orderId, OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        for (var item : orderDetail.items()) {
            var opt = item.operator();
            var ii = this.itemInfoRepository.findByOrderIdAndBusinessIdAndBusinessType(orderId, item.businessId(), item.businessType()).orElseGet(() -> new OrderItemInfoEntity().setOrderId(orderId)
                    .setBusinessType(item.businessType()).setBusinessId(item.businessId()).setProductName(item.context().getProductName()).setSpecId(item.specId()).setSpecName(item.context().getSpecRecord().specName())
                    .setNumber(item.number()).setApportion(item.apportion()).setParentBusinessId(item.parentBusinessId()).setExtendInfo(Jackson.writeString(item.extendInfo())).setOriginPrice(opt.getOriginalSinglePrice())
            ).setAdditional(item.additional() ? 1 : 0).setWebViewToast(item.webViewToast()).setCallbackAddress(item.callbackAddress());
            ii = this.itemInfoRepository.saveAndFlush(ii);
            if (!StringUtils.hasText(ii.getOrderItemId())) {
                throw new BusinessErrorException(CORE_OPERATE_ERROR, "error.order.orderItemSaveFail");
            }
            item.context().setOrderItemId(ii.getOrderItemId());
        }
        chain.postCalculate(orderId, orderDetail, chain, holder, target);
        for (var item : orderDetail.items()) {
            var opt = item.operator();
            var ife = this.itemFeeRepository.findByOrderIdAndOrderItemId(orderId, item.context().getOrderItemId()).orElseGet(() -> new OrderItemFeeEntity().setOrderId(orderId).setOrderItemId(item.context().getOrderItemId())
                    .setBusinessType(item.businessType())).setNumber(item.number()).setOriginSinglePrice(opt.getOriginalSinglePrice()).setOriginTotalPrice(opt.getOriginalTotalPrice()).setAfterDiscountSinglePrice(opt.getAfterDiscountSinglePrice())
                    .setAfterDiscountTotalPrice(opt.getAfterDiscountTotalPrice()).setUseDirect(opt.getUseDirect()).setUseWallet(opt.getUseWallet()).setDueCollectPrice(opt.getDueCollectPrice())
                    .setDueCollectSinglePrice(opt.getDueCollectSinglePrice()).setChargeCategory(item.chargeCategory().chargeCategory()).setChargeItemId(item.chargeCategory().chargeItemId())
                    .setMatchedChargeCategory(Jackson.writeString(opt.getCalculatedChargeCategory().stream().map(MatchResultDesAdapter::new).toList()));
            this.itemFeeRepository.saveAndFlush(ife);
        }
    }

    @Override
    public void preAsyncElastic(String orderId, OrderSearchInfoEntity entity, TargetUser target) {
        // 保存所有计算好的收费类型
        Function<OrderItemInfoEntity, SearchedOrderItemInfo> function = i -> this.itemFeeRepository.findByOrderIdAndOrderItemId(orderId, i.getOrderItemId())
                .map(of -> new SearchedOrderItemInfo(of.getOrderItemId(), i.getBusinessType(), i.getBusinessId(), i.getProductName(), i.getSpecName(), of.getChargeCategory())).orElseThrow();
        var items = this.itemInfoRepository.findByOrderId(orderId).stream().map(function).toList();
        CalculateProcessor.super.preAsyncElastic(orderId, entity.setItems(items), target);
    }

    @Override
    public void postConsumerElastic(Map<String, OrderSummaryTransObject> objects) {
        var query = new JPAQuery<>(this.entityManager);
        QOrderItemInfoEntity qoi = QOrderItemInfoEntity.orderItemInfoEntity;
        QOrderItemFeeEntity qof = QOrderItemFeeEntity.orderItemFeeEntity;
        var tuple = query.select(qoi, qof).from(qoi).leftJoin(qof).on(qoi.orderItemId.eq(qof.orderItemId)).where(qoi.orderId.in(objects.keySet())).fetch();
        var map = tuple.stream().map(t -> new OrderItemSummaryTransObject(t.get(qoi), new OrderItemFeeSummaryTransObject(t.get(qof)))).collect(Collectors.groupingBy(OrderItemSummaryTransObject::getOrderId));
        objects.keySet().stream().filter(map::containsKey).forEach(key -> objects.get(key).setItems(map.get(key)));
        CalculateProcessor.super.postConsumerElastic(objects);
    }

}
