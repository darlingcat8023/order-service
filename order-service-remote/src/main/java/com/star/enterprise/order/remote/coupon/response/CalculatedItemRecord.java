package com.star.enterprise.order.remote.coupon.response;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/
 */
public record CalculatedItemRecord(

        @JsonAlias(value = "use_coupons")
        BigDecimal useCoupons

) {}
