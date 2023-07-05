package com.star.enterprise.order.transfer.constants;

import com.star.enterprise.order.base.EnumSerialize;
import com.star.enterprise.order.core.handler.order.StatusTransactionAsyncHandler;
import com.star.enterprise.order.transfer.event.OrderTransferInfoAsyncEvent;
import com.star.enterprise.order.transfer.handler.OrderTransferCancelHandler;
import com.star.enterprise.order.transfer.handler.OrderTransferFinishHandler;
import com.star.enterprise.order.transfer.handler.OrderTransferStatusHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
@AllArgsConstructor
public enum OrderTransferStatusEnum implements EnumSerialize {

    //已完成
    FINISH("finish", "已完成") {
        @Override
        public OrderTransferStatusHandler getHandler(ApplicationContext context) {
            return context.getBean(OrderTransferFinishHandler.class);
        }
        @Override
        public StatusTransactionAsyncHandler<OrderTransferInfoAsyncEvent> getTransactionHandler(ApplicationContext context) {
            return context.getBean(OrderTransferFinishHandler.class);
        }
    },

    CANCEL("cancel", "已取消") {
        @Override
        public OrderTransferStatusHandler getHandler(ApplicationContext context) {
            return context.getBean(OrderTransferCancelHandler.class);
        }
        @Override
        public StatusTransactionAsyncHandler<OrderTransferInfoAsyncEvent> getTransactionHandler(ApplicationContext context) {
            return context.getBean(OrderTransferCancelHandler.class);
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
    public static OrderTransferStatusEnum of(String method) {
        return Arrays.stream(values()).filter(e -> e.value().equals(method)).findAny().orElse(null);
    }

    public abstract OrderTransferStatusHandler getHandler(ApplicationContext context);

    public abstract StatusTransactionAsyncHandler<OrderTransferInfoAsyncEvent> getTransactionHandler(ApplicationContext context);

}
