package com.star.enterprise.order.core.constants;

import com.star.enterprise.order.base.EnumSerialize;
import com.star.enterprise.order.core.event.OrderInfoAsyncEvent;
import com.star.enterprise.order.core.handler.order.*;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * 订单状态
 * @author xiaowenrou
 * @date 2022/9/26
 */
@AllArgsConstructor
public enum OrderStatusEnum implements EnumSerialize {

    // 待支付
    TO_BE_PAID("to_be_paid", "待支付") {
        @Override
        public StatusTransactionAsyncHandler<OrderInfoAsyncEvent> getTransactionHandler(final ApplicationContext applicationContext) {
            return applicationContext.getBean(OrderSaveHandler.class);
        }
        @Override
        public OrderStatusHandler getHandler(final ApplicationContext applicationContext) {
            return applicationContext.getBean(OrderSaveHandler.class);
        }
    },

    // 已保存
    PROCESS("process", "支付中") {
        @Override
        public StatusTransactionAsyncHandler<OrderInfoAsyncEvent> getTransactionHandler(final ApplicationContext applicationContext) {
            return applicationContext.getBean(OrderProcessHandler.class);
        }
        @Override
        public OrderStatusHandler getHandler(final ApplicationContext applicationContext) {
            return applicationContext.getBean(OrderProcessHandler.class);
        }
    },

    // 已支付
    PAID("paid", "已支付") {
        @Override
        public StatusTransactionAsyncHandler<OrderInfoAsyncEvent> getTransactionHandler(final ApplicationContext applicationContext) {
            return applicationContext.getBean(OrderPaidHandler.class);
        }

        @Override
        public OrderStatusHandler getHandler(final ApplicationContext applicationContext) {
            return applicationContext.getBean(OrderPaidHandler.class);
        }
    },

    // 已取消
    CANCEL("cancel", "已取消") {
        @Override
        public StatusTransactionAsyncHandler<OrderInfoAsyncEvent> getTransactionHandler(final ApplicationContext applicationContext) {
            return applicationContext.getBean(OrderCancelHandler.class);
        }

        @Override
        public OrderStatusHandler getHandler(final ApplicationContext applicationContext) {
            return applicationContext.getBean(OrderCancelHandler.class);
        }
    },

    // 已删除
    DELETE("delete", "已删除") {
        @Override
        public StatusTransactionAsyncHandler<OrderInfoAsyncEvent> getTransactionHandler(final ApplicationContext applicationContext) {
            return applicationContext.getBean(OrderDeleteHandler.class);
        }

        @Override
        public OrderStatusHandler getHandler(ApplicationContext applicationContext) {
            return applicationContext.getBean(OrderDeleteHandler.class);
        }
    }
    ;

    private final String value;

    private final String desc;

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public String desc() {
        return this.desc;
    }

    /**
     * 获取订单状态
     * @param method
     * @return
     */
    public static OrderStatusEnum of(String method) {
        return Arrays.stream(values()).filter(e -> e.value().equals(method)).findAny().orElse(null);
    }

    /**
     * 获取订单事务处理器
     * @param applicationContext
     * @return
     */
    public abstract StatusTransactionAsyncHandler<OrderInfoAsyncEvent> getTransactionHandler(final ApplicationContext applicationContext);

    /**
     * 获取状态处理器
     * @param applicationContext
     * @return
     */
    public abstract OrderStatusHandler getHandler(final ApplicationContext applicationContext);

}
