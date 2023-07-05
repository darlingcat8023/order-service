package com.star.enterprise.order.receipt.event;

import com.star.enterprise.order.message.base.IReceiptStatusMessage;
import com.star.enterprise.order.receipt.model.ReceiptContext;
import com.star.enterprise.order.receipt.service.Receipt;

/**
 * @author xiaowenrou
 * @date 2022/12/1
 */
public record ReceiptAsyncEvent(

        Receipt receipt,

        ReceiptContext context

) implements IReceiptStatusMessage {

    @Override
    public String receiptNo() {
        return this.receipt.receiptNo();
    }

    @Override
    public String receiptType() {
        return this.receipt.type().value();
    }

    @Override
    public String orderId() {
        return this.receipt.orderId();
    }

    @Override
    public String status() {
        return this.receipt.status().desc();
    }

    @Override
    public String targetId() {
        return this.receipt.target().targetId();
    }

    @Override
    public String campus() {
        return this.receipt.target().campus();
    }
}
