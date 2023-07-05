package com.star.enterprise.order.refund.model;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public interface OrderRefundExtendInfo extends OrderRefundInfo {

    /**
     * 附加信息
     * @return
     */
    RefundExtendInfo extendInfo();

}
