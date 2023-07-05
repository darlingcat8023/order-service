package com.star.enterprise.order.transfer.event.listener;

import com.star.enterprise.order.base.support.EventMethodInvoker;
import com.star.enterprise.order.transfer.event.OrderTransferInfoAsyncEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;
import static org.springframework.transaction.event.TransactionPhase.AFTER_ROLLBACK;

/**
 * order事件监听器
 * @author xiaowenrou
 * @date 2022/10/31
 */
@Slf4j
@Component
@AllArgsConstructor
public class OrderTransferInfoEventListener {

    private final ApplicationContext applicationContext;

    /**
     * 异步处理，使用指定的线程池
     * @param event
     */
    @Async(value = "eventTaskExecutor")
    @TransactionalEventListener(classes = OrderTransferInfoAsyncEvent.class,phase = AFTER_COMMIT)
    public void listenCommit(OrderTransferInfoAsyncEvent event) {
        log.info("order transfer commit event receive = {}", event);
        EventMethodInvoker.invoke(() -> event.transferStatus().getTransactionHandler(this.applicationContext).onCurrentStatusCommitTransactionAsync(event));
    }

    @Async(value = "eventTaskExecutor")
    @TransactionalEventListener(classes = OrderTransferInfoAsyncEvent.class,phase = AFTER_ROLLBACK)
    public void listenRollback(OrderTransferInfoAsyncEvent event) {
        log.info("order transfer rollback event receive event = {}", event);
        EventMethodInvoker.invoke(() -> event.transferStatus().getTransactionHandler(this.applicationContext).onCurrentStatusRollbackTransactionAsync(event));
    }

}
