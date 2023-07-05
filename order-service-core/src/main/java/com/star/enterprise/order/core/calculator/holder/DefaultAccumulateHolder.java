package com.star.enterprise.order.core.calculator.holder;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.core.calculator.CalculatorDelayTask;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.star.enterprise.order.base.exception.RestCode.CORE_OPERATE_ERROR;

/**
 * @author xiaowenrou
 * @date 2022/9/27
 */
@Getter
@ToString
@EqualsAndHashCode
public class DefaultAccumulateHolder implements AccumulateHolder {


    private BigDecimal useCoupons = new BigDecimal(0);

    private BigDecimal orderTotalPrice = new BigDecimal(0);

    private BigDecimal orderAfterDiscountTotalPrice = new BigDecimal(0);

    private BigDecimal orderDueCollectPrice = new BigDecimal(0);

    private final List<CalculatorDelayTask> delayTasks = new ArrayList<>();

    @Override
    public void addUseCoupons(BigDecimal price) {
        this.useCoupons = this.getUseCoupons().add(price);
    }

    public void addOrderTotalPrice(BigDecimal price) {
        this.orderTotalPrice = this.getOrderTotalPrice().add(price);
    }

    public void addOrderAfterDiscountTotalPrice(BigDecimal price) {
        this.orderAfterDiscountTotalPrice = this.getOrderAfterDiscountTotalPrice().add(price);
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
        if (delayTask != null) {
            this.delayTasks.add(delayTask);
        }
    }

}
