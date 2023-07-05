package com.star.enterprise.order.message.base;

/**
 * @author xiaowenrou
 * @date 2023/3/13
 */
public interface IRefundOrderStatusMessage {

    String refundOrderId();

    String status();

    String targetId();

    String campusId();

}
