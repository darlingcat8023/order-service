package com.star.enterprise.order.core.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;

import java.util.Map;

/**
 * order异步服务
 * @author xiaowenrou
 * @date 2022/12/6
 */
public interface OrderAsyncService {

    /**
     * 同步es的方法
     * @param orderId
     * @param entity
     */
    void asyncElastic(String orderId, TargetUser target, OrderSearchInfoEntity entity);

    /**
     * 消费es的方法
     * @param objects
     */
    void consume(Map<String, OrderSummaryTransObject> objects);

}
