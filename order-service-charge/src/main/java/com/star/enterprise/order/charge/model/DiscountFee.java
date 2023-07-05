package com.star.enterprise.order.charge.model;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public interface DiscountFee {

    /**
     * 使用直减
     * @return
     */
    BigDecimal getUseDirect();

    /**
     * 使用电子钱包
     * @return
     */
    BigDecimal getUseWallet();

}
