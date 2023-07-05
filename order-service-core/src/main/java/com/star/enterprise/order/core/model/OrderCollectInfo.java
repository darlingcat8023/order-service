package com.star.enterprise.order.core.model;

import com.star.enterprise.order.core.calculator.OrderFeeDetail;

/**
 * @author xiaowenrou
 * @date 2022/9/22
 */
public interface OrderCollectInfo extends OrderExtendInfo {

    /**
     * 订单内容详情
     * @return
     */
    OrderFeeDetail payload();


}
