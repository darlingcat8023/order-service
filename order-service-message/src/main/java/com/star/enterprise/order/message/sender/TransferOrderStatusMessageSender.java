package com.star.enterprise.order.message.sender;

import com.star.enterprise.message.template.MessageRecord;
import com.star.enterprise.message.template.order.TransferOrderStatusMessageRecord;
import com.star.enterprise.order.base.utils.DateTimeUtils;
import com.star.enterprise.order.message.TopicConstants;
import com.star.enterprise.order.message.base.ITransferOrderStatusMessage;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author xiaowenrou
 * @date 2023/3/13
 */
@Component
public class TransferOrderStatusMessageSender extends AbstractMessageSender<ITransferOrderStatusMessage> {

    protected TransferOrderStatusMessageSender(KafkaOperations<String, Object> operations) {
        super(operations);
    }

    @Override
    public @NonNull String topic() {
        return TopicConstants.TRANSFER_ORDER_STATUS_TOPIC;
    }

    @Override
    public @NonNull MessageRecord convert(@NonNull ITransferOrderStatusMessage message) {
        var current = DateTimeUtils.convertTimestamp(LocalDateTime.now());
        return new TransferOrderStatusMessageRecord(1, current, message.transferOrderId(), message.status(), message.targetId(), message.campusId());
    }

}
