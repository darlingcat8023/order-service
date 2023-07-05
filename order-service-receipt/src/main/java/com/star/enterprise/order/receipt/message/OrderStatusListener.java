package com.star.enterprise.order.receipt.message;

import com.star.enterprise.message.template.order.OrderStatusMessageRecord;
import com.star.enterprise.order.core.constants.OrderStatusEnum;
import com.star.enterprise.order.core.service.OrderService;
import com.star.enterprise.order.receipt.model.ReceiptContext;
import com.star.enterprise.order.receipt.service.ReceiptOrderPaidService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.star.enterprise.order.message.TopicConstants.ORDER_STATUS_TOPIC;

/**
 * @author xiaowenrou
 * @date 2023/2/1
 */
@Slf4j
@Component
@AllArgsConstructor
public class OrderStatusListener {

    private final ApplicationContext applicationContext;

    /**
     * 监听订单状态
     * @param record
     */
    @KafkaListener(topics = {ORDER_STATUS_TOPIC}, groupId = "order-system-inner")
    public void listen(ConsumerRecord<String, OrderStatusMessageRecord> record) {
        log.info("message receive topic = {}, message = {}", ORDER_STATUS_TOPIC, record);
        var type = OrderStatusEnum.of(record.value().getStatus());
        try {
            OrderStatusMessageHandler.getHandler(type, this.applicationContext).onMessage(record.value());
        } catch (Exception e) {
            log.error("message process fail", e);
        }
    }

    static abstract class OrderStatusMessageHandler {

        static OrderStatusMessageHandler getHandler(OrderStatusEnum status, ApplicationContext applicationContext) {
            var clazz = switch (status) {
                case PAID -> OrderPaidMessageHandler.class;
                case CANCEL -> OrderCancelMessageHandler.class;
                default -> EmptyHandler.class;
            };
            return applicationContext.getBean(clazz);
        }

        abstract void onMessage(OrderStatusMessageRecord record);

    }

    @Component
    static final class EmptyHandler extends OrderStatusMessageHandler {

        @Override
        public void onMessage(OrderStatusMessageRecord record) {}

    }

    @Component
    @AllArgsConstructor
    static final class OrderPaidMessageHandler extends OrderStatusMessageHandler {

        private final OrderService orderService;

        private final ReceiptOrderPaidService receiptService;

        @Override
        void onMessage(OrderStatusMessageRecord record) {
            this.orderService.findOrderInfo(record.getOrderId()).ifPresent(order -> this.receiptService.createReceipt(record.getOrderId(), order.getTarget(), new ReceiptContext()));
        }

    }

    @Component
    @AllArgsConstructor
    static final class OrderCancelMessageHandler extends OrderStatusMessageHandler {

        @Override
        void onMessage(OrderStatusMessageRecord record) {}

    }

}
