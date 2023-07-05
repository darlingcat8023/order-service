package com.star.enterprise.order.core.calculator.processor;

import com.star.enterprise.order.base.Paired;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculateProcessor;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.calculator.holder.DiscountPlanDelegateHolder;
import com.star.enterprise.order.core.data.jpa.OrderDiscountPlanCacheRepository;
import com.star.enterprise.order.core.data.jpa.entity.OrderDiscountPlanCacheEntity;
import com.star.enterprise.order.core.model.trans.OrderDiscountPlanTransObject;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 使用优惠方案
 * @author xiaowenrou
 * @date 2022/9/26
 */
@Slf4j
@Component
@AllArgsConstructor
public class DiscountPlanProcessor implements CalculateProcessor {

    @Override
    public int getOrder() {
        return 500;
    }

    private final OrderDiscountPlanCacheRepository cacheRepository;

    @Override
    public void preCalculate(OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        var items = orderDetail.items();
        holder.setDelegate(new DiscountPlanDelegateHolder(holder.getDelegate()));
        for (var item : items) {
            var discountPlan = item.discountPlan();
            var operator = item.operator();
            if (discountPlan != null && StringUtils.hasText(discountPlan.discountPlanId())) {
                // 设置折后价格
                operator.setAfterDiscountSinglePrice(discountPlan.afterDiscountSinglePrice()).setAfterDiscountTotalPrice(discountPlan.afterDiscountTotalPrice())
                        .setDueCollectPrice(operator.getAfterDiscountTotalPrice()).setDueCollectSinglePrice(operator.getAfterDiscountSinglePrice());
            }
            // 前面已经计算过一次订单总价了，这里使用一个委托累加器代理，在委托中计算新的折后总价
            holder.addOrderAfterDiscountTotalPrice(operator.getAfterDiscountTotalPrice());
            holder.addOrderDueCollectPrice(operator.getAfterDiscountTotalPrice());
        }
        chain.preCalculate(orderDetail, chain, holder, target);
    }

    @Override
    public void postCalculate(String orderId, OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        this.cacheRepository.deleteByOrderId(orderId);
        var list = new ArrayList<OrderDiscountPlanCacheEntity>();
        for (var item : orderDetail.items()) {
            var ctx = item.context();
            var plan = item.discountPlan();
            if (plan != null && StringUtils.hasText(plan.discountPlanId())) {
                var entity = new OrderDiscountPlanCacheEntity().setOrderId(orderId).setOrderItemId(ctx.getOrderItemId()).setDirectDiscountId(plan.discountPlanId()).setDirectDiscountName(plan.context().getDiscountPlanName())
                        .setDiscountPlanRate(plan.context().getDiscountPlanRate()).setDiscountSinglePrice(plan.afterDiscountSinglePrice()).setDiscountTotalPrice(plan.afterDiscountTotalPrice()).setApportion(plan.apportion());
                list.add(entity);
            }
        }
        if (!CollectionUtils.isEmpty(list)) {
            this.cacheRepository.saveAllAndFlush(list);
        }
        chain.postCalculate(orderId, orderDetail, chain, holder, target);
    }

    @Override
    public void postConsumerElastic(Map<String, OrderSummaryTransObject> objects) {
        Function<OrderDiscountPlanCacheEntity, Paired<String, String>> keySupplier = ent -> Paired.of(ent.getOrderId(), ent.getOrderItemId());
        var map = this.cacheRepository.findByOrderIdIn(objects.keySet()).stream().collect(Collectors.toMap(keySupplier, Function.identity()));
        objects.forEach((orderId, summary) -> {
            summary.getItems().forEach(item -> {
                var key = Paired.of(orderId, item.getOrderItemId());
                if (map.containsKey(key)) {
                    var value = map.get(key);
                    item.getItemFee().setDiscountPlanObject(new OrderDiscountPlanTransObject(value.getDirectDiscountId(), value.getDirectDiscountName(), value.getDiscountPlanRate()));
                }
            });
        });
        CalculateProcessor.super.postConsumerElastic(objects);
    }

}
