package com.star.enterprise.order.refund.calculator;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.refund.data.es.entity.OrderRefundSearchInfoEntity;
import com.star.enterprise.order.refund.model.OrderRefundInfo;
import com.star.enterprise.order.refund.model.trans.OrderRefundSummaryTransObject;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public interface OrderRefundCalculator {

    /**
     * 前置计算
     * @param target
     * @param refundInfo
     * @param holder
     */
    void preCalculate(TargetUser target, OrderRefundInfo refundInfo, RefundAccumulateHolder holder);

    /**
     * 后置计算
     * @param refundOrderId
     * @param target
     * @param refundInfo
     * @param holder
     */
    void postCalculate(String refundOrderId, TargetUser target, OrderRefundInfo refundInfo, RefundAccumulateHolder holder);

    /**
     * 同步es的数据构建
     * @param refundOrderId
     * @param entity
     * @param target
     * @return
     */
    default void preAsyncElastic(String refundOrderId, OrderRefundSearchInfoEntity entity, TargetUser target) {}

    /**
     * 消费es的数据构建
     * @param objects
     */
    default void postConsumerElastic(Map<String, OrderRefundSummaryTransObject> objects) {}

}
