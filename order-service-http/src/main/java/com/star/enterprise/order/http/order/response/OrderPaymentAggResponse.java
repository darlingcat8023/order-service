package com.star.enterprise.order.http.order.response;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/12/20
 */
public record OrderPaymentAggResponse(

        String paymentMethod,

        BigDecimal amount

) {}
