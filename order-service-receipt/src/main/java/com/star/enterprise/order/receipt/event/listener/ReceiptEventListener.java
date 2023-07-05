package com.star.enterprise.order.receipt.event.listener;

import com.star.enterprise.order.base.support.EventMethodInvoker;
import com.star.enterprise.order.receipt.event.ReceiptAsyncEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;
import static org.springframework.transaction.event.TransactionPhase.AFTER_ROLLBACK;

/**
 * @author xiaowenrou
 * @date 2022/12/1
 */
@Slf4j
@Component
@AllArgsConstructor
public class ReceiptEventListener {

    private final ApplicationContext applicationContext;

    /**
     * 异步处理，使用指定的线程池
     * @param event
     */
    @Async(value = "eventTaskExecutor")
    @TransactionalEventListener(classes = ReceiptAsyncEvent.class, phase = AFTER_COMMIT)
    public void listenCommit(ReceiptAsyncEvent event) {
        log.info("receipt commit event receive = {}", event);
        var receipt = event.receipt();
        EventMethodInvoker.invoke(() -> receipt.status().getTransactionHandler(this.applicationContext).onCurrentStatusCommitTransactionAsync(event));
    }

    /**
     * 回滚处理器
     * @param event
     */
    @Async(value = "eventTaskExecutor")
    @TransactionalEventListener(classes = ReceiptAsyncEvent.class, phase = AFTER_ROLLBACK)
    public void listenRollback(ReceiptAsyncEvent event) {
        log.info("receipt rollback event receive = {}", event);
        var receipt = event.receipt();
        EventMethodInvoker.invoke(() -> receipt.status().getTransactionHandler(this.applicationContext).onCurrentStatusRollbackTransactionAsync(event));
    }

}
