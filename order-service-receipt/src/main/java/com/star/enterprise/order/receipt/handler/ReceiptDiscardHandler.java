package com.star.enterprise.order.receipt.handler;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.core.handler.order.StatusTransactionAsyncHandler;
import com.star.enterprise.order.core.model.EmployeeUser;
import com.star.enterprise.order.receipt.event.ReceiptAsyncEvent;
import com.star.enterprise.order.receipt.service.Receipt;
import com.star.enterprise.order.receipt.service.ReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.star.enterprise.order.base.exception.RestCode.RECEIPT_ITEM_NOT_ALLOW;

/**
 * @author xiaowenrou
 * @date 2022/12/1
 */
@Component
@AllArgsConstructor
public class ReceiptDiscardHandler implements ReceiptStatusHandler, StatusTransactionAsyncHandler<ReceiptAsyncEvent> {

    private final ReceiptCreatedHandler delegate;

    @Override
    public void onCurrentStatusCommitTransactionAsync(ReceiptAsyncEvent event) {
        this.delegate.onCurrentStatusCommitTransactionAsync(event);
    }

    @Override
    public void processReceiptModify(ReceiptService<?> service, Receipt receipt, EmployeeUser operator, Runnable runnable) {
        throw new BusinessWarnException(RECEIPT_ITEM_NOT_ALLOW, "error.receipt.discarded");
    }

    @Override
    public void processReceiptDiscard(ReceiptService<?> service, Receipt receipt, EmployeeUser operator, Runnable runnable) {
        throw new BusinessWarnException(RECEIPT_ITEM_NOT_ALLOW, "error.receipt.discarded");
    }

}
