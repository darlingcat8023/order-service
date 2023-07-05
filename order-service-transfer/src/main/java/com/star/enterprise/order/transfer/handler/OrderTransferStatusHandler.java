package com.star.enterprise.order.transfer.handler;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferInfoEntity;
import com.star.enterprise.order.transfer.event.OrderTransferInfoAsyncEvent;
import com.star.enterprise.order.transfer.model.TransferExtendInfo;

import static com.star.enterprise.order.base.exception.RestCode.TRANSFER_ITEM_NOT_ALLOW;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
public interface OrderTransferStatusHandler {

    /**
     * 处理订单修改
     * @param transferOrderId
     * @param extendInfo
     * @param entity
     */
    void processOrderTransferModify(String transferOrderId, TransferExtendInfo extendInfo, OrderTransferInfoEntity entity);

    /**
     * 处理订单取消
     * @param transferOrderId
     * @param entity
     */
    void processOrderTransferCancel(String transferOrderId, OrderTransferInfoEntity entity);

    /**
     * 处理订单删除
     * @param transferOrderId
     * @param entity
     */
    default void processOrderTransferDelete(String transferOrderId, OrderTransferInfoEntity entity) {
        throw new BusinessWarnException(TRANSFER_ITEM_NOT_ALLOW, "error.transfer.deleteOperationNotAllow");
    }

}
