package com.star.enterprise.order.transfer.calculator;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.transfer.data.es.entity.OrderTransferSearchInfoEntity;
import com.star.enterprise.order.transfer.model.OrderTransferInfo;
import com.star.enterprise.order.transfer.model.trans.OrderTransferSummaryTransObject;
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
public class OrderTransferCalculatorChain implements OrderTransferCalculator {

    public final List<OrderTransferCalculator> delegates;

    public static OrderTransferCalculator createCalculator(ApplicationContext context) {
        var list = new ArrayList<OrderTransferCalculator>();
        list.add(context.getBean(TransferOrderTargetProcessor.class));
        list.add(context.getBean(TransferOrderItemProcessor.class));
        list.add(context.getBean(TransferOtherFeeProcessor.class));
        return new OrderTransferCalculatorChain(list);
    }

    @Override
    public void preCalculate(TargetUser target, OrderTransferInfo transferInfo, TransferAccumulateHolder holder) {
        this.delegates.forEach(proc -> proc.preCalculate(target, transferInfo, holder));
    }

    @Override
    public void postCalculate(String transferOrderId, TargetUser target, OrderTransferInfo refundInfo, TransferAccumulateHolder holder) {
        this.delegates.forEach(proc -> proc.postCalculate(transferOrderId, target, refundInfo, holder));
    }

    @Override
    public void preAsyncElastic(String transferOrderId, OrderTransferSearchInfoEntity entity, TargetUser target) {
        this.delegates.forEach(proc -> proc.preAsyncElastic(transferOrderId, entity, target));
    }

    @Override
    public void postConsumerElastic(Map<String, OrderTransferSummaryTransObject> objects) {
        this.delegates.forEach(proc -> proc.postConsumerElastic(objects));
    }

}
