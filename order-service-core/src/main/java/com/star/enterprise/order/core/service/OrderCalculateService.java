package com.star.enterprise.order.core.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculatorProcessorChain;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.holder.AccumulateHolder;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.calculator.provider.CalculatorProcessorProvider;
import com.star.enterprise.order.core.constants.OrderSourceEnum;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订单价格计算服务
 * @author xiaowenrou
 * @date 2022/9/23
 */
@Service
@AllArgsConstructor
public class OrderCalculateService implements OrderAsyncService {

    private final ApplicationContext applicationContext;

    public <V> V preOrderCalculate(TargetUser target, OrderFeeDetail feeDetail, BiFunction<OrderFeeDetail, AccumulateHolder, V> function) {
        var holder = DelegatingAccumulateHolder.newInstance();
        var chain = CalculatorProcessorChain.preCalculateChain(this.applicationContext);
        chain.preCalculate(feeDetail, chain, holder, target);
        return function.apply(feeDetail, holder);
    }

    public <V> V orderCalculate(TargetUser target, OrderFeeDetail feeDetail, BiFunction<OrderFeeDetail, AccumulateHolder, V> function) {
        var holder = DelegatingAccumulateHolder.newInstance();
        var chain = CalculatorProcessorProvider.supply(this.applicationContext, feeDetail);
        chain.preCalculate(feeDetail, chain, holder, target);
        return function.apply(feeDetail, holder);
    }

    @Override
    public void asyncElastic(String orderId, TargetUser target, OrderSearchInfoEntity entity) {
        var chain = CalculatorProcessorProvider.supply(this.applicationContext, OrderSourceEnum.of(entity.getOrderSource()));
        chain.preAsyncElastic(orderId, entity, target);
    }

    @Override
    public void consume(Map<String, OrderSummaryTransObject> objects) {
        var grouping = objects.values().stream().collect(Collectors.groupingBy(OrderSummaryTransObject::getOrderSource, Collectors.toMap(OrderSummaryTransObject::getOrderId, Function.identity())));
        grouping.forEach((source, map) -> CalculatorProcessorProvider.supply(this.applicationContext, OrderSourceEnum.of(source)).postConsumerElastic(map));
    }

}
