package com.star.enterprise.order.refund.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.refund.calculator.OrderRefundCalculatorChain;
import com.star.enterprise.order.refund.calculator.RefundAccumulateHolder;
import com.star.enterprise.order.refund.data.es.entity.OrderRefundSearchInfoEntity;
import com.star.enterprise.order.refund.model.OrderRefundInfo;
import com.star.enterprise.order.refund.model.trans.OrderRefundSummaryTransObject;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
@Service
@AllArgsConstructor
public class OrderRefundCalculateService implements OrderRefundAsyncService {

    private final ApplicationContext context;

    public <V> V calculate(TargetUser target, OrderRefundInfo refundInfo, BiFunction<OrderRefundInfo, RefundAccumulateHolder, V> biFunction) {
        var holder = new RefundAccumulateHolder();
        var chain = OrderRefundCalculatorChain.createCalculator(this.context);
        chain.preCalculate(target, refundInfo, holder);
        return biFunction.apply(refundInfo, holder);
    }

    @Override
    public void asyncElastic(String refundOrderId, TargetUser target, OrderRefundSearchInfoEntity entity) {
        var chain = OrderRefundCalculatorChain.createCalculator(this.context);
        chain.preAsyncElastic(refundOrderId, entity, target);
    }

    @Override
    public void consume(Map<String, OrderRefundSummaryTransObject> objects) {
        var chain = OrderRefundCalculatorChain.createCalculator(this.context);
        chain.postConsumerElastic(objects);
    }

}
