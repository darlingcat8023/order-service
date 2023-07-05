package com.star.enterprise.order.http.order.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public record DiscountInfoResponseRecord(

        BigDecimal useDirectDiscount,

        List<CouponDiscountRecord> couponDiscount,

        WalletDiscountRecord walletDiscount

) {}
