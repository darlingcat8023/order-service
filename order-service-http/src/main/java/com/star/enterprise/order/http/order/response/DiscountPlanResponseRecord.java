package com.star.enterprise.order.http.order.response;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/11/9
 */
public record DiscountPlanResponseRecord(

        String discountPlanId,

        String discountPlanName,

        BigDecimal discountPlanRate,

        Integer discountApportion

) {}
