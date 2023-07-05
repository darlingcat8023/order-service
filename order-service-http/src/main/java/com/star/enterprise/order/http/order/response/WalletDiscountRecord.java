package com.star.enterprise.order.http.order.response;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/15
 */
public record WalletDiscountRecord(

        BigDecimal useWallet,

        BigDecimal currentBalance

) {
}
