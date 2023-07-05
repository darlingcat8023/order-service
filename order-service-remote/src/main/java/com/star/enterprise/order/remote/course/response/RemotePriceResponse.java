package com.star.enterprise.order.remote.course.response;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public record RemotePriceResponse(

        @JsonAlias(value = {"price", "match_cost", "sell_price"})
        BigDecimal originalPrice,

        @JsonAlias(value = {"name"})
        String productName,

        @JsonAlias(value = {"spec_name", "specification_name"})
        String specName

) {}
