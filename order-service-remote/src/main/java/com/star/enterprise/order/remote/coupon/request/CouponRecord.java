package com.star.enterprise.order.remote.coupon.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @author xiaowenrou
 * @date 2023/3/20
 */
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CouponRecord(

        String templateId,

        String couponCode,

        Integer order

) {}
