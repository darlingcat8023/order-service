package com.star.enterprise.order.refund.event.listener;

import com.star.enterprise.order.base.support.EventMethodInvoker;
import com.star.enterprise.order.refund.event.OrderRefundInfoAsyncEvent;
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
public class OrderRefundInfoEventListener {

    private final ApplicationContext applicationContext;

    /**
     * 异步处理，使用指定的线程池
     * @param event
     */
    @Async(value = "eventTaskExecutor")
    @TransactionalEventListener(classes = OrderRefundInfoAsyncEvent.class, phase = AFTER_COMMIT)
    public void listenCommit(OrderRefundInfoAsyncEvent event) {
        log.info("order refund commit event receive = {}", event);
        EventMethodInvoker.invoke(() -> event.refundStatus().getTransactionHandler(this.applicationContext).onCurrentStatusCommitTransactionAsync(event));
    }

    /**
     * 异步处理，使用指定的线程池
     * @param event
     */
    @Async(value = "eventTaskExecutor")
    @TransactionalEventListener(classes = OrderRefundInfoAsyncEvent.class, phase = AFTER_ROLLBACK)
    public void listenRollback(OrderRefundInfoAsyncEvent event) {
        log.info("order refund rollback event receive = {}", event);
        EventMethodInvoker.invoke(() -> event.refundStatus().getTransactionHandler(this.applicationContext).onCurrentStatusRollbackTransactionAsync(event));
    }

}
