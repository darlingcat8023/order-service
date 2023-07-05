package com.star.enterprise.order.core.calculator;

import java.util.function.Consumer;

/**
 * 订单保存成功之后的附加逻辑
 * @author xiaowenrou
 * @date 2022/10/26
 */
@FunctionalInterface
public interface CalculatorDelayTask extends Consumer<String> {}
