package com.star.enterprise.order.message.sender;

import com.star.enterprise.message.template.MessageRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author xiaowenrou
 * @date 2023/1/30
 */
public abstract class AbstractMessageSender<M> {

    private final KafkaOperations<String, Object> operations;

    protected AbstractMessageSender(KafkaOperations<String, Object> operations) {
        this.operations = operations;
    }

    /**
     * 消息主题
     * @return
     */
    public abstract @NonNull String topic();

    /**
     * 消息体
     * @param message
     * @return
     */
    public abstract @NonNull MessageRecord convert(@NonNull M message);

    /**
     * 使用自定义key发送消息
     * @param key
     * @param message
     */
    public void sendMessage(@NonNull String key, @NonNull M message) {
        Assert.hasText(key, "must have a message key");
        var record = this.convert(message);
        this.operations.send(this.topic(), key, record).addCallback(new MessageSenderCallback());
    }

    /**
     * 监听消息发送的回调
     */
    @Slf4j
    private static final class MessageSenderCallback implements ListenableFutureCallback<SendResult<String, Object>> {

        @Override
        public void onFailure(@NonNull Throwable ex) {
            log.error("kafka send fail", ex);
        }

        @Override
        public void onSuccess(SendResult<String, Object> result) {
            if (result != null) {
                var producerRecord = result.getProducerRecord();
                log.info("kafka send success, topic = {}, message = {}", producerRecord.topic(), producerRecord.value());
            }
        }

    }

}
