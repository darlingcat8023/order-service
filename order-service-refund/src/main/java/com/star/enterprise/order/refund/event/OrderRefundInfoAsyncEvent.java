package com.star.enterprise.order.refund.event;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculatorDelayTask;
import com.star.enterprise.order.message.base.IRefundOrderStatusMessage;
import com.star.enterprise.order.refund.constants.OrderRefundStatusEnum;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/10/31
 */
public record OrderRefundInfoAsyncEvent(

        String refundOrderId,

        OrderRefundStatusEnum refundStatus,

        Integer version,

        TargetUser target,

        List<CalculatorDelayTask> delayTasks

) implements IRefundOrderStatusMessage {

    @Override
    public String status() {
        return this.refundStatus.value();
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
