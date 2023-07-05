package com.star.enterprise.order.refund.processor;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import com.star.enterprise.order.refund.model.OrderItemDelegate;

/**
 * @author xiaowenrou
 * @date 2023/3/2
 */
public interface OrderItemPredicateProcessor {

    /**
     * 全部匹配
     */
    OrderItemPredicateProcessor FALSE = (var1, var2, var3) -> false;

    /**
     * 全部不匹配
     */
    OrderItemPredicateProcessor TRUE = (var1, var2, var3) -> true;

    /**
     * 订单项过滤器
     * @param target
     * @param current
     * @param orderObject
     * @return
     */
    boolean predicate(TargetUser target, OrderItemDelegate current, OrderSummaryTransObject orderObject);

    /**
     * 订单项过滤器
     * @param target
     * @param current
     * @return
     */
    default boolean predicate(TargetUser target, OrderItemDelegate current) {
        return this.predicate(target, current, null);
    }

}
