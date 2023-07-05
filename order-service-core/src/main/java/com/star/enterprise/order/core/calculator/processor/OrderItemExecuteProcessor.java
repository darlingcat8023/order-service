package com.star.enterprise.order.core.calculator.processor;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculateProcessor;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2022/11/10
 */
@Slf4j
@Component
@AllArgsConstructor
public class OrderItemExecuteProcessor implements CalculateProcessor {

    /**
     * 委托父类 PriorityOrdered 的方法
     */
    @Delegate(types = {PriorityOrdered.class})
    private final OrderItemSaveProcessor delegate;

    @Override
    public void preCalculate(OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        this.delegate.preCalculate(orderDetail, chain, holder, target);
    }

    @Override
    public void postCalculate(String orderId, OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        this.delegate.postCalculate(orderId, orderDetail, chain, holder, target);
        orderDetail.items().forEach(item -> item.context().getHandler().processItemAfterSaved(orderId, target, item, holder));
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
