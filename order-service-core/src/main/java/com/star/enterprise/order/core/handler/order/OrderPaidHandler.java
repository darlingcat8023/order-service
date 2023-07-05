package com.star.enterprise.order.core.handler.order;

import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.core.calculator.CalculatorDelayTask;
import com.star.enterprise.order.core.calculator.processor.ChargeCategoryProcessor;
import com.star.enterprise.order.core.calculator.provider.CalculatorProcessorProvider;
import com.star.enterprise.order.core.constants.OrderSourceEnum;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.event.OrderInfoAsyncEvent;
import com.star.enterprise.order.core.model.OrderExtendInfo;
import com.star.enterprise.order.core.service.OrderAdditionalService;
import com.star.enterprise.order.core.service.OrderPaymentService;
import com.star.enterprise.order.core.service.OrderVerifyService;
import com.star.enterprise.order.core.utils.ApplicationContextUtils;
import com.star.enterprise.order.message.base.IOrderStatusMessage;
import com.star.enterprise.order.message.sender.OrderStatusMessageSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.Function;

import static com.star.enterprise.order.core.constants.OrderStatusEnum.CANCEL;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * @author xiaowenrou
 * @date 2022/11/3
 */
@Slf4j
@Component
@AllArgsConstructor
public class OrderPaidHandler implements OrderStatusHandler, StatusTransactionAsyncHandler<OrderInfoAsyncEvent> {

    private final ApplicationContext applicationContext;

    private final OrderSaveHandler delegate;

    private final ChargeCategoryProcessor categoryProcessor;

    private final OrderPaymentService paymentService;

    private final OrderAdditionalService additionalService;

    private final OrderStatusMessageSender messageSender;

    private final ThreadPoolTaskExecutor asyncTaskExecutor;

    @Override
    public void onCurrentStatusCommitTransactionAsync(final OrderInfoAsyncEvent event) {
        this.delegate.onCurrentStatusCommitTransactionAsync(event);
        this.messageSender.sendMessage(event.orderId(), event);
        // 执行延迟任务
        Function<CalculatorDelayTask, Runnable> function = task -> () -> task.accept(event.orderId());
        event.delayTasks().stream().map(function).forEach(this.asyncTaskExecutor::execute);
    }

    @Override
    public void onCurrentStatusRollbackTransactionAsync(final OrderInfoAsyncEvent event) {
        this.messageSender.sendMessage(event.orderId(), new IOrderStatusMessage() {
            @Override
            public String orderId() {return event.orderId();}

            @Override
            public String status() {return CANCEL.value();}

            @Override
            public String targetId() {return event.targetId();}

            @Override
            public String campusId() {return event.campusId();}
        });
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void processOrderModify(String orderId, OrderInfoEntity entity, OrderExtendInfo extendInfo, Map<String, MatchResult> categories) {
        // 修改的前置校验
        var verify = ApplicationContextUtils.getBeans(this.applicationContext, OrderVerifyService.class);
        verify.forEach(service -> service.beforeExistOrderModify(orderId, entity.getTarget(), extendInfo, entity));
        // 处理收费类型
        this.categoryProcessor.processChargeCategoryModify(orderId, entity.getTarget(), categories);
        this.paymentService.savePayments(orderId, extendInfo.payments());
        this.additionalService.saveInvoice(orderId, extendInfo.invoice());
        verify.forEach(service -> service.afterExistOrderModify(orderId, entity.getTarget(), extendInfo, entity));
    }

    @Override
    public void processOrderCancel(String orderId, OrderInfoEntity entity) {
        var verify = ApplicationContextUtils.getBeans(this.applicationContext, OrderVerifyService.class);
        verify.forEach(service -> service.beforeOrderRollback(entity.getOrderId(), entity.getTarget(), entity));
        var chain = CalculatorProcessorProvider.supply(this.applicationContext, OrderSourceEnum.of(entity.getOrderSource()));
        chain.postRollbackProcess(orderId, entity.getTarget(), chain);
        this.delegate.processOrderCancel(orderId, entity);
        verify.forEach(service -> service.afterOrderRollback(entity.getOrderId(), entity.getTarget(), entity));
    }

}
