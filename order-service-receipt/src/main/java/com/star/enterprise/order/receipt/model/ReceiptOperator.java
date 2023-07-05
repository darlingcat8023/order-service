package com.star.enterprise.order.receipt.model;

import java.time.LocalDateTime;

/**
 * @author xiaowenrou
 * @date 2022/12/19
 */
public interface ReceiptOperator {

    LocalDateTime getCreatedAt();

    String getReceiptNo();

    String getOperator();

    String getAction();

}
