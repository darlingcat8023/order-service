package com.star.enterprise.order.transfer.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.transfer.data.es.entity.OrderTransferSearchInfoEntity;
import com.star.enterprise.order.transfer.model.trans.OrderTransferSummaryTransObject;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
public interface OrderTransferAsyncService {

    /**
     * 同步es的方法
     * @param transferOrderId
     * @param target
     * @param entity
     */
    void asyncElastic(String transferOrderId, TargetUser target, OrderTransferSearchInfoEntity entity);

    /**
     * 组装数据
     * @param objects
     */
    void consume(Map<String, OrderTransferSummaryTransObject> objects);

}
