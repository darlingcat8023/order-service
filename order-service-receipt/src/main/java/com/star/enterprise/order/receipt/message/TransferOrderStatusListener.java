package com.star.enterprise.order.receipt.message;

import com.star.enterprise.message.template.order.TransferOrderStatusMessageRecord;
import com.star.enterprise.order.receipt.model.ReceiptContext;
import com.star.enterprise.order.receipt.service.ReceiptTransferOrderService;
import com.star.enterprise.order.transfer.constants.OrderTransferStatusEnum;
import com.star.enterprise.order.transfer.service.OrderTransferService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.star.enterprise.order.message.TopicConstants.TRANSFER_ORDER_STATUS_TOPIC;

/**
 * @author xiaowenrou
 * @date 2023/3/13
 */
@Slf4j
@AllArgsConstructor
@Component
public class TransferOrderStatusListener {
    
    private final ApplicationContext applicationContext;

    /**
     * 监听订单状态
     * @param record
     */
    @KafkaListener(topics = {TRANSFER_ORDER_STATUS_TOPIC}, groupId = "order-system-inner")
    public void listen(ConsumerRecord<String, TransferOrderStatusMessageRecord> record) {
        log.info("message receive topic = {}, message = {}", TRANSFER_ORDER_STATUS_TOPIC, record);
        var type = OrderTransferStatusEnum.of(record.value().getStatus());
        try {
            TransferOrderStatusMessageHandler.getHandler(type, this.applicationContext).onMessage(record.value());
        } catch (Exception e) {
            log.error("message process fail", e);
        }
    }

    static abstract class TransferOrderStatusMessageHandler {

        static TransferOrderStatusMessageHandler getHandler(OrderTransferStatusEnum status, ApplicationContext applicationContext) {
            var clazz = switch (status) {
                case FINISH -> TransferOrderFinishMessageHandler.class;
                case CANCEL -> EmptyTransferOrderMessageHandler.class;
            };
            return applicationContext.getBean(clazz);
        }

        abstract void onMessage(TransferOrderStatusMessageRecord record);

    }

    @Component
    @AllArgsConstructor
    static final class TransferOrderFinishMessageHandler extends TransferOrderStatusMessageHandler {

        private final OrderTransferService orderService;

        private final ReceiptTransferOrderService receiptService;

        @Override
        void onMessage(TransferOrderStatusMessageRecord record) {
            this.orderService.findTransferOrderInfo(record.getTransferOrderId()).ifPresent(order -> this.receiptService.createReceipt(record.getTransferOrderId(), order.getTarget(), new ReceiptContext()));
        }

    }

    @Component
    static final class EmptyTransferOrderMessageHandler extends TransferOrderStatusMessageHandler {

        @Override
        void onMessage(TransferOrderStatusMessageRecord record) {}

    }

}
