package com.star.enterprise.order.transfer.handler;

import com.star.enterprise.order.base.exception.TaskExecuteWarnException;
import com.star.enterprise.order.core.calculator.CalculatorDelayTask;
import com.star.enterprise.order.core.handler.order.StatusTransactionAsyncHandler;
import com.star.enterprise.order.message.sender.TransferOrderStatusMessageSender;
import com.star.enterprise.order.transfer.data.jpa.OrderTransferInfoRepository;
import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferInfoEntity;
import com.star.enterprise.order.transfer.event.OrderTransferInfoAsyncEvent;
import com.star.enterprise.order.transfer.model.TransferExtendInfo;
import com.star.enterprise.order.transfer.service.OrderTransferAdditionalService;
import com.star.enterprise.order.transfer.service.OrderTransferSearchService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

import static com.star.enterprise.order.transfer.constants.OrderTransferStatusEnum.CANCEL;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Component
@AllArgsConstructor
public class OrderTransferFinishHandler implements OrderTransferStatusHandler, StatusTransactionAsyncHandler<OrderTransferInfoAsyncEvent> {

    private final OrderTransferInfoRepository infoRepository;

    private final OrderTransferSearchService service;

    private final OrderTransferAdditionalService additionalService;

    private final TransferOrderStatusMessageSender messageSender;

    private final ThreadPoolTaskExecutor asyncTaskExecutor;


    @Override
    public void onCurrentStatusCommitTransactionAsync(OrderTransferInfoAsyncEvent event) {
        var entity = this.infoRepository.findByTransferOrderId(event.transferOrderId()).filter(o -> event.version().equals(o.getVersion()))
                .orElseThrow(() -> new TaskExecuteWarnException("order transfer sync es fail : version not match"));
        this.service.refresh(event.transferOrderId(), event.target(), entity);
        this.messageSender.sendMessage(event.transferOrderId(), event);
        Function<CalculatorDelayTask, Runnable> function = task -> () -> task.accept(event.transferOrderId());
        event.delayTasks().stream().map(function).forEach(this.asyncTaskExecutor::execute);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void processOrderTransferModify(String transferOrderId, TransferExtendInfo extendInfo, OrderTransferInfoEntity entity) {
        this.additionalService.saveAdditionalInfo(transferOrderId, extendInfo);
    }

    @Override
    public void processOrderTransferCancel(String transferOrderId, OrderTransferInfoEntity entity) {
        this.infoRepository.saveAndFlush(entity.setTransferStatus(CANCEL.value()));
    }

}
