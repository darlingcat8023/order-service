package com.star.enterprise.order.remote.coupon.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/20
 */
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CouponCalculateItemRecord(

        String contextId,

        String businessId,

        String businessType,

        Integer number,

        Integer apportion,

        BigDecimal afterDiscountSinglePrice,

        BigDecimal afterDiscountTotalPrice,

        BigDecimal dueCollectSinglePrice,

        BigDecimal dueCollectTotalPrice

) {}
