package com.star.enterprise.order.remote.callback;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/2/15
 */
public record BusinessFeeRecord(

        /*
         * 标准单价
         */
        BigDecimal originSinglePrice,

        /*
         * 标准单价
         */
        BigDecimal originTotalPrice,

        /*
         * 购买单价
         */
        BigDecimal paidSinglePrice,

        /*
         * 购买总价
         */
        BigDecimal paidTotalPrice

) {}
