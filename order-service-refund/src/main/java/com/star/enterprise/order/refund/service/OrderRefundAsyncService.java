package com.star.enterprise.order.refund.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.refund.data.es.entity.OrderRefundSearchInfoEntity;
import com.star.enterprise.order.refund.model.trans.OrderRefundSummaryTransObject;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
public interface OrderRefundAsyncService {

    /**
     * 同步es的方法
     * @param refundOrderId
     * @param target
     * @param entity
     */
    void asyncElastic(String refundOrderId, TargetUser target, OrderRefundSearchInfoEntity entity);

    /**
     * 组装数据
     * @param objects
     */
    void consume(Map<String, OrderRefundSummaryTransObject> objects);

}
