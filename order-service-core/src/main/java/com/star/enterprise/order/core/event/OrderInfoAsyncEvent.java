package com.star.enterprise.order.core.event;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculatorDelayTask;
import com.star.enterprise.order.core.constants.OrderStatusEnum;
import com.star.enterprise.order.message.base.IOrderStatusMessage;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/10/31
 */
public record OrderInfoAsyncEvent (

        String orderId,

        OrderStatusEnum orderStatus,

        Integer version,

        TargetUser target,

        List<CalculatorDelayTask> delayTasks

) implements IOrderStatusMessage {

    @Override
    public String status() {
        return this.orderStatus.value();
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
