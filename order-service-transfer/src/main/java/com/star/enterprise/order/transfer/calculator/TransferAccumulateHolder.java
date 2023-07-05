package com.star.enterprise.order.transfer.calculator;

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
public class TransferAccumulateHolder {

    private final List<CalculatorDelayTask> delayTasks = new ArrayList<>();

    private BigDecimal dueTransferPrice = new BigDecimal(0);

    private BigDecimal finalTransferPrice = new BigDecimal(0);

    public void addDelayTask(CalculatorDelayTask task) {
        if (task != null) {
            this.delayTasks.add(task);
        }
    }

    public TransferAccumulateHolder addDueTransferPrice(BigDecimal price) {
        this.dueTransferPrice = this.dueTransferPrice.add(price);
        this.finalTransferPrice = this.finalTransferPrice.add(price);
        return this;
    }

    public TransferAccumulateHolder subtractDueTransferPrice(BigDecimal price) {
        this.dueTransferPrice = this.dueTransferPrice.subtract(price);
        this.finalTransferPrice = this.finalTransferPrice.subtract(price);
        return this;
    }

    public TransferAccumulateHolder subtractFinalTransferPrice(BigDecimal fee) {
        this.finalTransferPrice = this.finalTransferPrice.subtract(fee);
        return this;
    }

}
