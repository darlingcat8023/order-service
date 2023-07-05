package com.star.enterprise.order.remote.course.response;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/2
 */
public record RemoteArticleResponse(

        @JsonAlias(value = {"price", "match_cost", "sell_price"})
        BigDecimal originalPrice,

        @JsonAlias(value = {"name"})
        String productName,

        @JsonAlias(value = {"allow_refund"})
        Integer allowRefund,

        @JsonAlias(value = {"allow_carry_over"})
        Integer allowTransfer

) {
}
