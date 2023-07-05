package com.star.enterprise.order.message.sender;

import com.star.enterprise.message.template.MessageRecord;
import com.star.enterprise.message.template.order.OrderStatusMessageRecord;
import com.star.enterprise.order.base.utils.DateTimeUtils;
import com.star.enterprise.order.message.base.IOrderStatusMessage;
import com.star.enterprise.order.message.TopicConstants;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author xiaowenrou
 * @date 2023/1/30
 */
@Component
public class OrderStatusMessageSender extends AbstractMessageSender<IOrderStatusMessage> {

    protected OrderStatusMessageSender(KafkaOperations<String, Object> operations) {
        super(operations);
    }

    @Override
    public @NonNull String topic() {
        return TopicConstants.ORDER_STATUS_TOPIC;
    }

    @Override
    public @NonNull MessageRecord convert(@NonNull IOrderStatusMessage message) {
        var current = DateTimeUtils.convertTimestamp(LocalDateTime.now());
        return new OrderStatusMessageRecord(current, message.orderId(), message.status(), message.targetId(), message.campusId());
    }

}
