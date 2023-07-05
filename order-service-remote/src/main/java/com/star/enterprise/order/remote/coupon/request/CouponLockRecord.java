package com.star.enterprise.order.remote.coupon.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/21
 */
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CouponLockRecord(

        String orderId,

        List<CouponRecord> coupons

) {
}
