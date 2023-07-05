package com.star.enterprise.order.receipt.message;

import com.star.enterprise.message.template.order.RefundOrderStatusMessageRecord;
import com.star.enterprise.order.receipt.model.ReceiptContext;
import com.star.enterprise.order.receipt.service.ReceiptRefundOrderService;
import com.star.enterprise.order.refund.constants.OrderRefundStatusEnum;
import com.star.enterprise.order.refund.service.OrderRefundService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.star.enterprise.order.message.TopicConstants.REFUND_ORDER_STATUS_TOPIC;

/**
 * @author xiaowenrou
 * @date 2023/3/13
 */
@Slf4j
@Component
@AllArgsConstructor
public class RefundOrderStatusListener {

    private final ApplicationContext applicationContext;

    /**
     * 监听订单状态
     * @param record
     */
    @KafkaListener(topics = {REFUND_ORDER_STATUS_TOPIC}, groupId = "order-system-inner")
    public void listen(ConsumerRecord<String, RefundOrderStatusMessageRecord> record) {
        log.info("message receive topic = {}, message = {}", REFUND_ORDER_STATUS_TOPIC, record);
        var type = OrderRefundStatusEnum.of(record.value().getStatus());
        try {
            RefundOrderStatusMessageHandler.getHandler(type, this.applicationContext).onMessage(record.value());
        } catch (Exception e) {
            log.error("message process fail", e);
        }
    }

    static abstract class RefundOrderStatusMessageHandler {

        static RefundOrderStatusMessageHandler getHandler(OrderRefundStatusEnum status, ApplicationContext applicationContext) {
            var clazz = switch (status) {
                case FINISH -> RefundOrderFinishMessageHandler.class;
                case CANCEL -> EmptyRefundOrderMessageHandler.class;
            };
            return applicationContext.getBean(clazz);
        }

        abstract void onMessage(RefundOrderStatusMessageRecord record);

    }

    @Component
    @AllArgsConstructor
    static final class RefundOrderFinishMessageHandler extends RefundOrderStatusMessageHandler {

        private final OrderRefundService orderService;

        private final ReceiptRefundOrderService receiptService;

        @Override
        void onMessage(RefundOrderStatusMessageRecord record) {
            this.orderService.findRefundOrderInfo(record.getRefundOrderId()).ifPresent(order -> this.receiptService.createReceipt(record.getRefundOrderId(), order.getTarget(), new ReceiptContext()));
        }

    }

    @Component
    @AllArgsConstructor
    static final class EmptyRefundOrderMessageHandler extends RefundOrderStatusMessageHandler {

        @Override
        void onMessage(RefundOrderStatusMessageRecord record) {}

    }

}
