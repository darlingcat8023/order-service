package com.star.enterprise.order.receipt.handler;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.core.handler.order.StatusTransactionAsyncHandler;
import com.star.enterprise.order.core.model.EmployeeUser;
import com.star.enterprise.order.message.sender.ReceiptStatusMessageSender;
import com.star.enterprise.order.receipt.constants.ReceiptActionEnum;
import com.star.enterprise.order.receipt.event.ReceiptAsyncEvent;
import com.star.enterprise.order.receipt.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.star.enterprise.order.base.exception.RestCode.RECEIPT_ITEM_NOT_ALLOW;

/**
 * @author xiaowenrou
 * @date 2022/12/1
 */
@Component
@AllArgsConstructor
public class ReceiptCreatedHandler implements ReceiptStatusHandler, StatusTransactionAsyncHandler<ReceiptAsyncEvent> {

    private final ReceiptSearchService searchService;

    private final ReceiptSequenceService sequenceService;

    private final ReceiptLogService logService;

    private final ReceiptStatusMessageSender messageSender;

    @Override
    public void onCurrentStatusCommitTransactionAsync(ReceiptAsyncEvent event) {
        this.messageSender.sendMessage(event.receiptNo(),event);
        this.searchService.refresh(event.receipt(), event.context().getOperators());
    }

    @Override
    public void processReceiptModify(ReceiptService<?> service, Receipt receipt, EmployeeUser operator, Runnable runnable) {
        this.logService.addReceiptLog(receipt, operator, ReceiptActionEnum.MODIFY);
        if (runnable!= null) {
            runnable.run();
        }
    }

    @Override
    public void processReceiptDiscard(ReceiptService<?> service, Receipt receipt, EmployeeUser operator, Runnable runnable) {
        var last = this.sequenceService.latestReceiptNo(receipt.target()).orElse(receipt.receiptNo());
        if (!receipt.receiptNo().equals(last)) {
            throw new BusinessWarnException(RECEIPT_ITEM_NOT_ALLOW, "error.receipt.receiptDiscardNotAllow", last);
        }
        this.logService.addReceiptLog(receipt, operator, ReceiptActionEnum.DISCARD);
        this.sequenceService.deleteReceiptSequence(last);
        if (runnable != null) {
            runnable.run();
        }
    }

}
