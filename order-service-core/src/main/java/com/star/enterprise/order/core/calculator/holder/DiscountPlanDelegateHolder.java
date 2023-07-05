package com.star.enterprise.order.core.calculator.holder;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.core.calculator.CalculatorDelayTask;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import static com.star.enterprise.order.base.exception.RestCode.CORE_OPERATE_ERROR;

/**
 * @author xiaowenrou
 * @date 2023/3/17
 */
@RequiredArgsConstructor
public class DiscountPlanDelegateHolder implements AccumulateHolder {

    private final AccumulateHolder delegate;

    private BigDecimal orderAfterDiscountTotalPrice = new BigDecimal(0);

    private BigDecimal orderDueCollectPrice = new BigDecimal(0);

    @Override
    public BigDecimal getUseCoupons() {
        return this.delegate.getUseCoupons();
    }

    @Override
    public BigDecimal getOrderTotalPrice() {
        return this.delegate.getOrderTotalPrice();
    }

    @Override
    public BigDecimal getOrderAfterDiscountTotalPrice() {
        return this.orderAfterDiscountTotalPrice;
    }

    @Override
    public BigDecimal getOrderDueCollectPrice() {
        return this.orderDueCollectPrice;
    }

    @Override
    public void addUseCoupons(BigDecimal price) {
        this.delegate.addUseCoupons(price);
    }

    @Override
    public void addOrderTotalPrice(BigDecimal price) {
        this.delegate.addOrderTotalPrice(price);
    }

    @Override
    public void addOrderAfterDiscountTotalPrice(BigDecimal decimal) {
        this.orderAfterDiscountTotalPrice = this.getOrderAfterDiscountTotalPrice().add(decimal);
    }

    @Override
    public void addOrderDueCollectPrice(BigDecimal decimal) {
        this.orderDueCollectPrice = this.getOrderDueCollectPrice().add(decimal);
    }

    @Override
    public void subtractOrderDueCollectPrice(BigDecimal decimal) {
        if (this.orderDueCollectPrice.compareTo(decimal) < 0) {
            throw new BusinessWarnException(CORE_OPERATE_ERROR, "error.order.orderPriceNegative");
        }
        this.orderDueCollectPrice = this.getOrderDueCollectPrice().subtract(decimal);
    }

    @Override
    public void addDelayTask(CalculatorDelayTask delayTask) {
        this.delegate.addDelayTask(delayTask);
    }

    @Override
    public List<CalculatorDelayTask> getDelayTasks() {
        return this.delegate.getDelayTasks();
    }

}
