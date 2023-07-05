package com.star.enterprise.order.remote.coupon.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/20
 */
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CouponCalculateRecord(

        List<CouponCalculateItemRecord> items,

        List<CouponRecord> coupons

) {}
