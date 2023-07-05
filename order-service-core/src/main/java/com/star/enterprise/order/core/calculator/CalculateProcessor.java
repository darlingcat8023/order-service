package com.star.enterprise.order.core.calculator;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import org.springframework.core.PriorityOrdered;

import java.util.Map;

/**
 * 费用处理接口
 * @author xiaowenrou
 * @date 2022/9/23
 */
public interface CalculateProcessor extends PriorityOrdered {

    /**
     * 排序
     * @return
     */
    @Override
    default int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    /**
     * 前置处理器, 触发时机在住订单保存之前
     * @param orderDetail
     * @param chain
     * @param holder
     * @param target
     */
    void preCalculate(OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target);

    /**
     * 后置处理器，触发时机主订单保存之后
     * @param orderId
     * @param orderDetail
     * @param chain
     * @param holder
     * @param target
     */
    void postCalculate(String orderId, OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target);

    /**
     * 订单取消的回滚逻辑
     * @param orderId
     * @param target
     * @param chain
     */
    default void postRollbackProcess(String orderId, TargetUser target, CalculateProcessor chain) {
        chain.postRollbackProcess(orderId, target, chain);
    }

    /**
     * 同步es的数据构建
     * @param orderId
     * @param entity
     * @param target
     * @return
     */
    default void preAsyncElastic(String orderId, OrderSearchInfoEntity entity, TargetUser target) {}

    /**
     * 消费es的数据构建
     * @param objects
     */
    default void postConsumerElastic(Map<String, OrderSummaryTransObject> objects) {}

}
