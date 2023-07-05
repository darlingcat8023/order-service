package com.star.enterprise.order.transfer.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.transfer.calculator.OrderTransferCalculatorChain;
import com.star.enterprise.order.transfer.calculator.TransferAccumulateHolder;
import com.star.enterprise.order.transfer.data.es.entity.OrderTransferSearchInfoEntity;
import com.star.enterprise.order.transfer.model.OrderTransferInfo;
import com.star.enterprise.order.transfer.model.trans.OrderTransferSummaryTransObject;
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
public class OrderTransferCalculateService implements OrderTransferAsyncService {

    private final ApplicationContext context;

    public <V> V calculate(TargetUser target, OrderTransferInfo transferInfo, BiFunction<OrderTransferInfo, TransferAccumulateHolder, V> biFunction) {
        var holder = new TransferAccumulateHolder();
        var chain = OrderTransferCalculatorChain.createCalculator(this.context);
        chain.preCalculate(target, transferInfo, holder);
        return biFunction.apply(transferInfo, holder);
    }

    @Override
    public void asyncElastic(String refundOrderId, TargetUser target, OrderTransferSearchInfoEntity entity) {
        var chain = OrderTransferCalculatorChain.createCalculator(this.context);
        chain.preAsyncElastic(refundOrderId, entity, target);
    }

    @Override
    public void consume(Map<String, OrderTransferSummaryTransObject> objects) {
        var chain = OrderTransferCalculatorChain.createCalculator(this.context);
        chain.postConsumerElastic(objects);
    }

}
