package com.star.enterprise.order.refund.calculator;

import com.star.enterprise.order.core.calculator.CalculatorDelayTask;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
@Getter
public class RefundAccumulateHolder {

    private final List<CalculatorDelayTask> delayTasks = new ArrayList<>();

    private BigDecimal totalOverPrice = new BigDecimal(0);

    private BigDecimal dueRefundPrice = new BigDecimal(0);

    private BigDecimal finalRefundPrice = new BigDecimal(0);

    public void addDelayTask(CalculatorDelayTask task) {
        if (task != null) {
            this.delayTasks.add(task);
        }
    }

    public RefundAccumulateHolder addOverPrice(BigDecimal price) {
        this.totalOverPrice = this.totalOverPrice.add(price);
        return this;
    }

    public RefundAccumulateHolder addDueRefundPrice(BigDecimal price) {
        this.dueRefundPrice = this.dueRefundPrice.add(price);
        this.finalRefundPrice = this.finalRefundPrice.add(price);
        return this;
    }

    public RefundAccumulateHolder subtractDueRefundPrice(BigDecimal price) {
        this.dueRefundPrice = this.dueRefundPrice.subtract(price);
        this.finalRefundPrice = this.finalRefundPrice.subtract(price);
        return this;
    }

    public RefundAccumulateHolder subtractFinalRefundPrice(BigDecimal fee) {
        this.finalRefundPrice = this.finalRefundPrice.subtract(fee);
        return this;
    }

}
