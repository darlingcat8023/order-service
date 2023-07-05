package com.star.enterprise.order.transfer.event;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculatorDelayTask;
import com.star.enterprise.order.message.base.ITransferOrderStatusMessage;
import com.star.enterprise.order.transfer.constants.OrderTransferStatusEnum;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/10/31
 */
public record OrderTransferInfoAsyncEvent(

        String transferOrderId,

        OrderTransferStatusEnum transferStatus,

        Integer version,

        TargetUser target,

        List<CalculatorDelayTask> delayTasks

) implements ITransferOrderStatusMessage {

    @Override
    public String status() {
        return this.transferStatus.value();
    }

    @Override
    public String targetId() {
        return this.target.targetId();
    }

    @Override
    public String campusId() {
        return this.target.campus();
    }

}
