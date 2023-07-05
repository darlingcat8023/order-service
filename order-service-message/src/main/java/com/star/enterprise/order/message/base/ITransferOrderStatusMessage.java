package com.star.enterprise.order.message.base;

/**
 * @author xiaowenrou
 * @date 2023/3/13
 */
public interface ITransferOrderStatusMessage {

    String transferOrderId();

    String status();

    String targetId();

    String campusId();

}
