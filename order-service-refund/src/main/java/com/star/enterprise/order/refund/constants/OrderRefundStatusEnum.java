package com.star.enterprise.order.refund.constants;

import com.star.enterprise.order.base.EnumSerialize;
import com.star.enterprise.order.core.handler.order.StatusTransactionAsyncHandler;
import com.star.enterprise.order.refund.event.OrderRefundInfoAsyncEvent;
import com.star.enterprise.order.refund.handler.OrderRefundCancelHandler;
import com.star.enterprise.order.refund.handler.OrderRefundFinishHandler;
import com.star.enterprise.order.refund.handler.OrderRefundStatusHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
@AllArgsConstructor
public enum OrderRefundStatusEnum implements EnumSerialize {

    //已完成
    FINISH("finish", "已完成") {
        @Override
        public OrderRefundStatusHandler getHandler(ApplicationContext context) {
            return context.getBean(OrderRefundFinishHandler.class);
        }
        @Override
        public StatusTransactionAsyncHandler<OrderRefundInfoAsyncEvent> getTransactionHandler(ApplicationContext context) {
            return context.getBean(OrderRefundFinishHandler.class);
        }
    },
    CANCEL("cancel", "已取消") {
        @Override
        public OrderRefundStatusHandler getHandler(ApplicationContext context) {
            return context.getBean(OrderRefundCancelHandler.class);
        }
        @Override
        public StatusTransactionAsyncHandler<OrderRefundInfoAsyncEvent> getTransactionHandler(ApplicationContext context) {
            return context.getBean(OrderRefundCancelHandler.class);
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
    public static OrderRefundStatusEnum of(String method) {
        return Arrays.stream(values()).filter(e -> e.value().equals(method)).findAny().orElse(null);
    }

    public abstract OrderRefundStatusHandler getHandler(ApplicationContext context);

    public abstract StatusTransactionAsyncHandler<OrderRefundInfoAsyncEvent> getTransactionHandler(ApplicationContext context);

}
