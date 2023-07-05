package com.star.enterprise.order.message.sender;

import com.star.enterprise.message.template.MessageRecord;
import com.star.enterprise.message.template.order.RefundOrderStatusMessageRecord;
import com.star.enterprise.order.base.utils.DateTimeUtils;
import com.star.enterprise.order.message.TopicConstants;
import com.star.enterprise.order.message.base.IRefundOrderStatusMessage;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author xiaowenrou
 * @date 2023/3/13
 */
@Component
public class RefundOrderStatusMessageSender extends AbstractMessageSender<IRefundOrderStatusMessage> {

    protected RefundOrderStatusMessageSender(KafkaOperations<String, Object> operations) {
        super(operations);
    }

    @Override
    public @NonNull String topic() {
        return TopicConstants.REFUND_ORDER_STATUS_TOPIC;
    }

    @Override
    public @NonNull MessageRecord convert(@NonNull IRefundOrderStatusMessage message) {
        var current = DateTimeUtils.convertTimestamp(LocalDateTime.now());
        return new RefundOrderStatusMessageRecord(1, current, message.refundOrderId(), message.status(), message.targetId(), message.campusId());
    }

}
