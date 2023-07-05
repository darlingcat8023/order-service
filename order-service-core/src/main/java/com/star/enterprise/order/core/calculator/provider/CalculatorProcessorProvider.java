package com.star.enterprise.order.core.calculator.provider;

import com.star.enterprise.order.core.calculator.CalculateProcessor;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.constants.OrderSourceEnum;
import org.springframework.context.ApplicationContext;

/**
 * @author xiaowenrou
 * @date 2023/4/19
 */
public abstract sealed class CalculatorProcessorProvider permits FrontEndCalculatorProvider {

    /**
     * 默认的处理器实现
     * @param context
     * @param orderSource
     * @return
     */
    public static CalculateProcessor supply(ApplicationContext context, OrderSourceEnum orderSource) {
        return getProvider(context, orderSource).doSupply();
    }

    public static CalculateProcessor supply(ApplicationContext context, OrderFeeDetail detail) {
        var orderSource = OrderSourceEnum.of(detail.orderSource());
        return getProvider(context, orderSource).doSupply(detail);
    }

    public static CalculateProcessor supplyWithExecute(ApplicationContext context, OrderFeeDetail detail) {
        var orderSource = OrderSourceEnum.of(detail.orderSource());
        return getProvider(context, orderSource).doSupplyWithExecute(detail);
    }

    private static CalculatorProcessorProvider getProvider(ApplicationContext context, OrderSourceEnum orderSource) {
        return switch (orderSource) {
            case FRONT_END -> context.getBean(FrontEndCalculatorProvider.class);
        };
    }

    /**
     * 提供处理器的逻辑
     * @return
     */
    public abstract CalculateProcessor doSupply();

    public abstract CalculateProcessor doSupply(OrderFeeDetail detail);

    public abstract CalculateProcessor doSupplyWithExecute(OrderFeeDetail detail);

}
