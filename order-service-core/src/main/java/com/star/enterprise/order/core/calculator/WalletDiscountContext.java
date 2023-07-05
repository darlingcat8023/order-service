package com.star.enterprise.order.core.calculator;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/15
 */
@Data
@Accessors(chain = true)
public class WalletDiscountContext {

    private boolean canUse = false;

    private BigDecimal currentBalance;

    private String commitId;

}
