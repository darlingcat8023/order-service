package com.star.enterprise.order.refund.handler;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundInfoEntity;
import com.star.enterprise.order.refund.event.OrderRefundInfoAsyncEvent;
import com.star.enterprise.order.refund.model.RefundExtendInfo;

import static com.star.enterprise.order.base.exception.RestCode.REFUND_ITEM_NOT_ALLOW;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
public interface OrderRefundStatusHandler {

    /**
     * 处理退费修改
     * @param refundOrderId
     * @param extendInfo
     * @param entity
     */
    void processOrderRefundModify(String refundOrderId, RefundExtendInfo extendInfo, OrderRefundInfoEntity entity);

    /**
     * 处理订单取消
     * @param refundOrderId
     * @param entity
     */
    void processOrderRefundCancel(String refundOrderId, OrderRefundInfoEntity entity);

    /**
     * 处理订单删除
     * @param refundOrderId
     * @param entity
     */
    default void processOrderRefundDelete(String refundOrderId, OrderRefundInfoEntity entity) {
        throw new BusinessWarnException(REFUND_ITEM_NOT_ALLOW, "error.refund.deleteOperationNotAllow");
    }

}
