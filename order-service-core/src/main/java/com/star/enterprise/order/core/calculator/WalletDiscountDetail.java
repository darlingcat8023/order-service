package com.star.enterprise.order.core.calculator;

import java.math.BigDecimal;

/**
 * 零钱包优惠详情
 * @author xiaowenrou
 * @date 2023/3/15
 */
public interface WalletDiscountDetail {

    /**
     * 使用零钱包金额
     * @return
     */
    BigDecimal useWallet();

    /**
     * 零钱包上下文
     * @return
     */
    WalletDiscountContext context();

}
