package com.star.enterprise.order.core.model;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public interface PaymentInfo {

    /**
     * 订单号
     * @return
     */
    String getOrderId();

    /**
     * 支付方式
     * @return
     */
    String getPaymentMethod();

    /**
     * 付款账户
     * @return
     */
    String getPaymentAccount();

    /**
     * 付款金额
     * @return
     */
    BigDecimal getPaymentAmount();

}
