package com.star.enterprise.order.core.calculator.processor;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculateProcessor;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2022/10/26
 */
@Component
@AllArgsConstructor
public class ChargeCategoryExecuteProcessor implements CalculateProcessor {

    @Delegate(types = {PriorityOrdered.class})
    private final ChargeCategoryProcessor delegate;

    @Override
    public void preCalculate(OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        this.delegate.preCalculate(orderDetail, chain, holder, target);
    }

    @Override
    public void postCalculate(String orderId, OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        // 执行charge判断的回调方法
        for (var item : orderDetail.items()) {
            if (StringUtils.hasText(item.chargeCategory().chargeItemId())) {
                item.operator().getHooks().forEach(hook -> hook.accept(orderId, item.context().getOrderItemId(), item.chargeCategory().chargeItemId()));
            }
        }
        this.delegate.postCalculate(orderId, orderDetail, chain, holder, target);
    }

    @Override
    public void postRollbackProcess(String orderId, TargetUser target, CalculateProcessor chain) {
        this.delegate.postRollbackProcess(orderId, target, chain);
    }

    @Override
    public void preAsyncElastic(String orderId, OrderSearchInfoEntity entity, TargetUser target) {
        this.delegate.preAsyncElastic(orderId, entity, target);
    }

    @Override
    public void postConsumerElastic(Map<String, OrderSummaryTransObject> objects) {
        this.delegate.postConsumerElastic(objects);
    }
}
