package com.star.enterprise.order.message.base;

/**
 * @author xiaowenrou
 * @date 2023/4/11
 */
public interface IReceiptStatusMessage {

    String receiptNo();

    String receiptType();

    String orderId();

    String status();

    String targetId();

    String campus();

}
