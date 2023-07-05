package com.star.enterprise.order.core.calculator;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.calculator.processor.*;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2023/1/6
 */
public final class CalculatorProcessorChain implements CalculateProcessor {

    private final List<CalculateProcessor> processors;

    private final int len;

    private int index = 0;

    public static CalculateProcessor preCalculateChain(ApplicationContext context) {
        var list = new ArrayList<CalculateProcessor>();
        list.add(context.getBean(OrderItemSaveProcessor.class));
        list.add(context.getBean(DiscountPlanProcessor.class));
        return new CalculatorProcessorChain(list);
    }

    public CalculatorProcessorChain(List<CalculateProcessor> processors) {
        AnnotationAwareOrderComparator.sort(processors);
        this.processors = Collections.unmodifiableList(processors);
        this.len = processors.size();
    }

    @Override
    public void preCalculate(OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        if (this.index < this.len) {
            var proc = this.processors.get(this.index++);
            proc.preCalculate(orderDetail, this, holder, target);
        }
        this.index = 0;
    }

    @Override
    public void postCalculate(String orderId, OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        if (this.index < this.len) {
            var proc = this.processors.get(this.index++);
            proc.postCalculate(orderId, orderDetail, this, holder, target);
        }
        this.index = 0;
    }

    @Override
    public void postRollbackProcess(String orderId, TargetUser target, CalculateProcessor chain) {
        if (this.index < this.len) {
            var proc = this.processors.get(this.index++);
            proc.postRollbackProcess(orderId, target, this);
        }
        this.index = 0;
    }

    @Override
    public void preAsyncElastic(String orderId, OrderSearchInfoEntity entity, TargetUser target) {
        this.processors.forEach(proc -> proc.preAsyncElastic(orderId, entity, target));
    }

    @Override
    public void postConsumerElastic(Map<String, OrderSummaryTransObject> objects) {
        this.processors.forEach(proc -> proc.postConsumerElastic(objects));
    }

}
