package com.star.enterprise.order.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xiaowenrou
 * @date 2023/1/31
 */
@Slf4j
@Component
public class OrderListener {

//    @RetryableTopic(attempts = "2", backoff = @Backoff(value = 60000L), fixedDelayTopicStrategy = FixedDelayStrategy.SINGLE_TOPIC)
//    @KafkaListener(topics = {TopicConstants.ORDER_STATUS_TOPIC}, groupId = "order-system-inner")
//    public void listen(OrderStatusMessageRecord record) {
//        if (record.getRetryTimes() > 0) {
//            record.setRetryTimes(record.getRetryTimes() - 1);
//            log.info("do delay record = {}", record);
//            throw new DelayMessageNotActiveException();
//        }
//        log.info("delay rec, record = {}", record);
//    }

}
