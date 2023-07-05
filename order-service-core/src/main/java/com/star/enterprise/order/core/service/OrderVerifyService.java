package com.star.enterprise.order.core.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.holder.AccumulateHolder;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.model.OrderCollectInfo;
import com.star.enterprise.order.core.model.OrderExtendInfo;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * 订单生命周期前置验证逻辑
 * @author xiaowenrou
 * @date 2022/12/28
 */
public interface OrderVerifyService extends PriorityOrdered {

    @Override
    default int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * 订单计算前的验证
     * @param target
     * @param info
     */
    default void beforeCalculate(TargetUser target, OrderCollectInfo info) {}

    /**
     * 订单保存前的验证
     * @param orderId
     * @param target
     * @param info
     * @param holder
     */
    default void beforeOrderSaved(String orderId, TargetUser target, OrderCollectInfo info, AccumulateHolder holder) {}

    /**
     * 订单保存前的验证
     * @param orderId
     * @param target
     * @param info
     * @param holder
     */
    default void afterOrderSaved(String orderId, TargetUser target, OrderCollectInfo info, AccumulateHolder holder) {}


    /**
     * 订单收费前的验证
     * @param orderId
     * @param target
     * @param info
     * @param holder
     */
    default void beforeOrderPaid(String orderId, TargetUser target, OrderCollectInfo info, AccumulateHolder holder) {}

    /**
     * 订单收费前的验证
     * @param orderId
     * @param target
     * @param info
     * @param holder
     */
    default void afterOrderPaid(String orderId, TargetUser target, OrderCollectInfo info, AccumulateHolder holder) {}

    /**
     * 已有订单修改前的动作
     * @param orderId
     * @param targetUser
     * @param extendInfo
     * @param entity
     */
    default void beforeExistOrderModify(String orderId, TargetUser targetUser, OrderExtendInfo extendInfo, OrderInfoEntity entity) {}

    /**
     * 订单修改后的动作
     * @param orderId
     * @param targetUser
     * @param extendInfo
     * @param entity
     */
    default void afterExistOrderModify(String orderId, TargetUser targetUser, OrderExtendInfo extendInfo, OrderInfoEntity entity) {}

    /**
     * 订单取消前的动作
     * @param orderId
     * @param target
     * @param entity
     */
    default void beforeOrderRollback(String orderId, TargetUser target, OrderInfoEntity entity) {}

    /**
     * 订单取消后的动作
     * @param orderId
     * @param target
     * @param entity
     */
    default void afterOrderRollback(String orderId, TargetUser target, OrderInfoEntity entity) {}

}
