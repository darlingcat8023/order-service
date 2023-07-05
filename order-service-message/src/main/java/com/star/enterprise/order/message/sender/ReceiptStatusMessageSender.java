package com.star.enterprise.order.message.sender;

import com.star.enterprise.message.template.MessageRecord;
import com.star.enterprise.message.template.receipt.ReceiptStatusMessageRecord;
import com.star.enterprise.order.base.utils.DateTimeUtils;
import com.star.enterprise.order.message.TopicConstants;
import com.star.enterprise.order.message.base.IReceiptStatusMessage;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author xiaowenrou
 * @date 2023/4/11
 */
@Component
public class ReceiptStatusMessageSender extends AbstractMessageSender<IReceiptStatusMessage> {

    protected ReceiptStatusMessageSender(KafkaOperations<String, Object> operations) {
        super(operations);
    }

    @Override
    public @NonNull String topic() {
        return TopicConstants.RECEIPT_STATUS_TOPIC;
    }

    @Override
    public @NonNull MessageRecord convert(@NonNull IReceiptStatusMessage message) {
        var current = DateTimeUtils.convertTimestamp(LocalDateTime.now());
        return new ReceiptStatusMessageRecord(1, current, message.receiptNo(), message.receiptType(), message.orderId(), message.status(), message.targetId(), message.campus());
    }

}
