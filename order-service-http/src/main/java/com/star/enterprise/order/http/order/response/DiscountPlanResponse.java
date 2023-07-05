package com.star.enterprise.order.http.order.response;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/9/29
 */
public record DiscountPlanResponse(

        String planId,

        String planName,

        BigDecimal discount,

        Integer apportion

) {}
