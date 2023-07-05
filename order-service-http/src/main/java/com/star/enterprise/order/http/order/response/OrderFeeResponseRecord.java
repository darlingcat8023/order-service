package com.star.enterprise.order.http.order.response;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public record OrderFeeResponseRecord(

        BigDecimal useCoupons,

        BigDecimal orderOriginTotalPrice,

        BigDecimal orderAfterDiscountTotalPrice,

        BigDecimal orderDueCollectPrice

) {}
