package com.star.enterprise.order.transfer.calculator;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.transfer.data.es.entity.OrderTransferSearchInfoEntity;
import com.star.enterprise.order.transfer.model.OrderTransferInfo;
import com.star.enterprise.order.transfer.model.trans.OrderTransferSummaryTransObject;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public interface OrderTransferCalculator {

    /**
     * 前置计算
     * @param target
     * @param transferInfo
     * @param holder
     */
    void preCalculate(TargetUser target, OrderTransferInfo transferInfo, TransferAccumulateHolder holder);

    /**
     * 后置计算
     * @param transferOrderId
     * @param target
     * @param TransferInfo
     * @param holder
     */
    void postCalculate(String transferOrderId, TargetUser target, OrderTransferInfo TransferInfo, TransferAccumulateHolder holder);

    /**
     * 同步es的数据构建
     * @param transferOrderId
     * @param entity
     * @param target
     * @return
     */
    default void preAsyncElastic(String transferOrderId, OrderTransferSearchInfoEntity entity, TargetUser target) {}

    /**
     * 消费es的数据构建
     * @param objects
     */
    default void postConsumerElastic(Map<String, OrderTransferSummaryTransObject> objects) {}

}
