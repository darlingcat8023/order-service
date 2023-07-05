package com.star.enterprise.order.charge.matcher;

import com.star.enterprise.order.base.function.TupleConsumer;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
@FunctionalInterface
public interface ChargeHook extends TupleConsumer<String, String, String> {
}
