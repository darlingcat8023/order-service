package com.star.enterprise.order.refund.calculator;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.refund.data.es.entity.OrderRefundSearchInfoEntity;
import com.star.enterprise.order.refund.model.OrderRefundInfo;
import com.star.enterprise.order.refund.model.trans.OrderRefundSummaryTransObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderRefundCalculatorChain implements OrderRefundCalculator {

    public final List<OrderRefundCalculator> delegates;

    public static OrderRefundCalculator createCalculator(ApplicationContext context) {
        var list = new ArrayList<OrderRefundCalculator>();
        list.add(context.getBean(RefundOrderTargetProcessor.class));
        list.add(context.getBean(RefundOrderItemProcessor.class));
        list.add(context.getBean(RefundOtherFeeProcessor.class));
        list.add(context.getBean(RefundWalletProcessor.class));
        return new OrderRefundCalculatorChain(list);
    }

    @Override
    public void preCalculate(TargetUser target, OrderRefundInfo refundInfo, RefundAccumulateHolder holder) {
        this.delegates.forEach(proc -> proc.preCalculate(target, refundInfo, holder));
    }

    @Override
    public void postCalculate(String refundOrderId, TargetUser target, OrderRefundInfo refundInfo, RefundAccumulateHolder holder) {
        this.delegates.forEach(proc -> proc.postCalculate(refundOrderId, target, refundInfo, holder));
    }

    @Override
    public void preAsyncElastic(String refundOrderId, OrderRefundSearchInfoEntity entity, TargetUser target) {
        this.delegates.forEach(proc -> proc.preAsyncElastic(refundOrderId, entity, target));
    }

    @Override
    public void postConsumerElastic(Map<String, OrderRefundSummaryTransObject> objects) {
        this.delegates.forEach(proc -> proc.postConsumerElastic(objects));
    }

}
