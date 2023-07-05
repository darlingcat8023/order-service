package com.star.enterprise.order.core.model.impl;

import com.star.enterprise.order.charge.matcher.ChargeHook;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.core.calculator.OrderItemFeeOperator;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
@Data
@Accessors(chain = true)
public class DefaultFeeOperator implements OrderItemFeeOperator {

    private BigDecimal originalSinglePrice = new BigDecimal(0);

    private BigDecimal originalTotalPrice = new BigDecimal(0);

    private BigDecimal afterDiscountSinglePrice = new BigDecimal(0);

    private BigDecimal afterDiscountTotalPrice = new BigDecimal(0);

    private BigDecimal useDirect = new BigDecimal(0);

    private BigDecimal useCoupons = new BigDecimal(0);

    private BigDecimal useWallet = new BigDecimal(0);

    private BigDecimal dueCollectSinglePrice = new BigDecimal(0);

    private BigDecimal dueCollectPrice = new BigDecimal(0);

    private List<MatchResult> calculatedChargeCategory = new ArrayList<>();

    private List<ChargeHook> hooks = new CopyOnWriteArrayList<>();

    @Override
    public OrderItemFeeOperator addChargeCategory(MatchResult category) {
        this.getCalculatedChargeCategory().add(category);
        return this;
    }

    @Override
    public void addHooks(ChargeHook runnable) {
        this.hooks.add(runnable);
    }

}
