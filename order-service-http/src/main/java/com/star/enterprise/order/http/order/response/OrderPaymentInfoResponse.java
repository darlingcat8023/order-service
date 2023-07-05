package com.star.enterprise.order.http.order.response;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/10/27
 */
public record OrderPaymentInfoResponse(

        String method,

        String paymentAccount,

        BigDecimal paymentAmount

) {}
