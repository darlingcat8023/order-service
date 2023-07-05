package com.star.enterprise.order.core.calculator.processor;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculateProcessor;
import com.star.enterprise.order.core.calculator.CouponDetail;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.OrderItemFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.data.jpa.OrderCouponBindRepository;
import com.star.enterprise.order.core.data.jpa.entity.OrderCouponBindEntity;
import com.star.enterprise.order.core.model.trans.OrderCouponTransObject;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import com.star.enterprise.order.remote.coupon.RemoteCouponService;
import com.star.enterprise.order.remote.coupon.request.CouponCalculateItemRecord;
import com.star.enterprise.order.remote.coupon.request.CouponCalculateRecord;
import com.star.enterprise.order.remote.coupon.request.CouponRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 处理优惠券
 * @author xiaowenrou
 * @date 2022/11/7
 */
@Slf4j
@Component
@AllArgsConstructor
public class CouponProcessor implements CalculateProcessor {

    @Override
    public int getOrder() {
        return 1500;
    }

    private final RemoteCouponService couponService;

    private final OrderCouponBindRepository couponBindRepository;

    @Override
    public void preCalculate(OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        var coupons = orderDetail.discount().couponDiscount();
        if (coupons == null || CollectionUtils.isEmpty(coupons)) {
            chain.preCalculate(orderDetail, chain, holder, target);
            return;
        }
        Function<OrderItemFeeDetail, CouponCalculateItemRecord> iFunction = item -> {
            var operator = item.operator();
            var contextId = UUID.randomUUID().toString();
            item.context().setCouponCalculateContextId(contextId);
            return new CouponCalculateItemRecord(contextId, item.businessId(), item.businessType(), item.number(), item.apportion(), operator.getAfterDiscountSinglePrice(), operator.getAfterDiscountTotalPrice(),
                    operator.getDueCollectSinglePrice(), operator.getDueCollectPrice());
        };
        var records = orderDetail.items().stream().map(iFunction).toList();
        var discountRecords = coupons.stream().map(coupon -> new CouponRecord(coupon.templateId(), coupon.couponCode(), coupon.order())).toList();
        var ret = this.couponService.calculateCouponDiscount(target.targetId(), target.campus(), new CouponCalculateRecord(records, discountRecords));
        orderDetail.items().forEach(item -> {
            var contextId = item.context().getCouponCalculateContextId();
            if (!ret.containsKey(contextId)) {
                return;
            }
            var useCoupons = ret.get(contextId).useCoupons();
            var operator = item.operator();
            operator.setUseCoupons(useCoupons).setDueCollectPrice(operator.getDueCollectPrice().subtract(useCoupons));
            holder.addUseCoupons(useCoupons);
            holder.subtractOrderDueCollectPrice(useCoupons);
        });
        chain.preCalculate(orderDetail, chain, holder, target);
    }

    @Override
    public void postCalculate(String orderId, OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        var coupons = orderDetail.discount().couponDiscount();
        this.couponBindRepository.deleteByOrderId(orderId);
        if (coupons == null || CollectionUtils.isEmpty(coupons)) {
            chain.postCalculate(orderId, orderDetail, chain, holder, target);
            return;
        }
        Function<CouponDetail, OrderCouponBindEntity> function = coupon ->
                new OrderCouponBindEntity().setOrderId(orderId).setCouponOrder(coupon.order()).setTemplateId(coupon.templateId()).setCouponCode(coupon.couponCode()).setTemplateName(coupon.context().getTemplateName());
        this.couponBindRepository.saveAllAndFlush(coupons.stream().map(function).toList());
        chain.postCalculate(orderId, orderDetail, chain, holder, target);
    }

    @Override
    public void preAsyncElastic(String orderId, OrderSearchInfoEntity entity, TargetUser target) {
        // TODO 优惠券同步搜索引擎没有意义
        CalculateProcessor.super.preAsyncElastic(orderId, entity, target);
    }

    @Override
    public void postConsumerElastic(Map<String, OrderSummaryTransObject> objects) {
        Collector<OrderCouponBindEntity, ?, List<OrderCouponTransObject>> downStream = Collectors.mapping(c -> new OrderCouponTransObject(c.getTemplateId(), c.getTemplateName(), c.getCouponCode(), c.getCouponOrder()), Collectors.toList());
        var coupons = this.couponBindRepository.findByOrderIdIn(objects.keySet()).stream().collect(Collectors.groupingBy(OrderCouponBindEntity::getOrderId, downStream));
        objects.keySet().stream().filter(coupons::containsKey).forEach(key -> objects.get(key).setCoupons(coupons.get(key)));
        CalculateProcessor.super.postConsumerElastic(objects);
    }

}
