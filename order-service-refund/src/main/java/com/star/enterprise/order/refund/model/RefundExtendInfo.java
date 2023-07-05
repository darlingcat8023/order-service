package com.star.enterprise.order.refund.model;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public interface RefundExtendInfo {

    String refundMethod();

    String bankAccount();

    String bankId();

    String bankName();

    String cardNumber();

    String remark();

    String refundReason();

    List<String> files();

}
