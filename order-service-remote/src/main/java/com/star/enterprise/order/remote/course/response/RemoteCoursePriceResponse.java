package com.star.enterprise.order.remote.course.response;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/11/4
 */
public record RemoteCoursePriceResponse(

        String productName,

        BigDecimal standardPrice,

        SpecPriceRecord specPrice,

        @JsonAlias(value = {"allow_refund"})
        boolean allowRefund,

        @JsonAlias(value = {"allow_transfer"})
        boolean allowTransfer

) {}
