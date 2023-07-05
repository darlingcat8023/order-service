package com.star.enterprise.order.message.base;

/**
 * @author xiaowenrou
 * @date 2023/1/30
 */
public interface IOrderStatusMessage {

    String orderId();

    String status();

    String targetId();

    String campusId();

}
