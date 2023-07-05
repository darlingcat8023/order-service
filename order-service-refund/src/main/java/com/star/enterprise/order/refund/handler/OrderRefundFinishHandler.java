package com.star.enterprise.order.refund.handler;

import com.star.enterprise.order.base.exception.TaskExecuteWarnException;
import com.star.enterprise.order.core.calculator.CalculatorDelayTask;
import com.star.enterprise.order.core.handler.order.StatusTransactionAsyncHandler;
import com.star.enterprise.order.message.sender.RefundOrderStatusMessageSender;
import com.star.enterprise.order.refund.data.jpa.OrderRefundInfoRepository;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundInfoEntity;
import com.star.enterprise.order.refund.event.OrderRefundInfoAsyncEvent;
import com.star.enterprise.order.refund.model.RefundExtendInfo;
import com.star.enterprise.order.refund.service.OrderRefundAdditionalService;
import com.star.enterprise.order.refund.service.OrderRefundSearchService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

import static com.star.enterprise.order.refund.constants.OrderRefundStatusEnum.CANCEL;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Component
@AllArgsConstructor
public class OrderRefundFinishHandler implements OrderRefundStatusHandler, StatusTransactionAsyncHandler<OrderRefundInfoAsyncEvent> {

    private final OrderRefundInfoRepository infoRepository;

    private final OrderRefundSearchService service;

    private final OrderRefundAdditionalService additionalService;

    private final RefundOrderStatusMessageSender messageSender;

    private final ThreadPoolTaskExecutor asyncTaskExecutor;

    @Override
    public void onCurrentStatusCommitTransactionAsync(OrderRefundInfoAsyncEvent event) {
        var entity = this.infoRepository.findByRefundOrderId(event.refundOrderId()).filter(o -> event.version().equals(o.getVersion()))
                .orElseThrow(() -> new TaskExecuteWarnException("order refund sync es fail : version not match"));
        this.service.refresh(event.refundOrderId(), event.target(), entity);
        this.messageSender.sendMessage(event.refundOrderId(), event);
        Function<CalculatorDelayTask, Runnable> function = task -> () -> task.accept(event.refundOrderId());
        event.delayTasks().stream().map(function).forEach(this.asyncTaskExecutor::execute);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void processOrderRefundModify(String refundOrderId, RefundExtendInfo extendInfo, OrderRefundInfoEntity entity) {
        this.additionalService.saveAdditionalInfo(refundOrderId, extendInfo);
    }

    @Override
    public void processOrderRefundCancel(String refundOrderId, OrderRefundInfoEntity entity) {
        this.infoRepository.saveAndFlush(entity.setRefundStatus(CANCEL.value()));
    }

}
