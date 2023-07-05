package com.star.enterprise.order.core.calculator;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/9/29
 */
public interface DiscountDetail {

    /**
     * 使用直减
     * @return
     */
    BigDecimal useDirectDiscount();

    /**
     * 使用优惠券
     */
    List<CouponDetail> couponDiscount();

    /**
     * 使用电子钱包
     * @return
     */
    WalletDiscountDetail walletDiscount();

}
