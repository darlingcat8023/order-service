package com.star.enterprise.order.core.handler.order;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.model.OrderExtendInfo;

import java.util.Map;

import static com.star.enterprise.order.base.exception.RestCode.CORE_OPERATE_NOT_ALLOW;

/**
 * @author xiaowenrou
 * @date 2022/11/4
 */
public interface OrderStatusHandler {



    /**
     * 处理订单更新
     * @param orderId
     * @param entity
     * @param extendInfo
     */
    void processOrderModify(String orderId, OrderInfoEntity entity, OrderExtendInfo extendInfo, Map<String, MatchResult> categories);

    /**
     * 处理不同状态下的订单取消逻辑
     * @param orderId
     * @param entity
     */
    void processOrderCancel(String orderId, OrderInfoEntity entity);

    /**
     * 处理不同状态下的订单删除逻辑
     * @param orderId
     * @param entity
     */
    default void processOrderDelete(String orderId, OrderInfoEntity entity) {
        throw new BusinessWarnException(CORE_OPERATE_NOT_ALLOW, "error.order.deleteOperationNotAllow");
    }

}
