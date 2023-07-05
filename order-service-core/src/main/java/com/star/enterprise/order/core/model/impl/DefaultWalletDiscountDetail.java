package com.star.enterprise.order.core.model.impl;

import com.star.enterprise.order.core.calculator.WalletDiscountContext;
import com.star.enterprise.order.core.calculator.WalletDiscountDetail;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/15
 */
@Data
@RequiredArgsConstructor
public class DefaultWalletDiscountDetail implements WalletDiscountDetail {

    private final BigDecimal useWallet;

    private final WalletDiscountContext context = new WalletDiscountContext();

    @Override
    public BigDecimal useWallet() {
        return this.useWallet;
    }

    @Override
    public WalletDiscountContext context() {
        return this.context;
    }

}
