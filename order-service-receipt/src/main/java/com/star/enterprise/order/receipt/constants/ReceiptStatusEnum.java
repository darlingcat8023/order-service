package com.star.enterprise.order.receipt.constants;

import com.star.enterprise.order.base.EnumSerialize;
import com.star.enterprise.order.core.handler.order.StatusTransactionAsyncHandler;
import com.star.enterprise.order.receipt.event.ReceiptAsyncEvent;
import com.star.enterprise.order.receipt.handler.ReceiptCreatedHandler;
import com.star.enterprise.order.receipt.handler.ReceiptDiscardHandler;
import com.star.enterprise.order.receipt.handler.ReceiptStatusHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * @author xiaowenrou
 * @date 2022/11/29
 */
@AllArgsConstructor
public enum ReceiptStatusEnum implements EnumSerialize {

    CREATED("created", "已创建") {
        @Override
        public ReceiptStatusHandler getHandler(ApplicationContext applicationContext) {
            return applicationContext.getBean(ReceiptCreatedHandler.class);
        }
        @Override
        public StatusTransactionAsyncHandler<ReceiptAsyncEvent> getTransactionHandler(ApplicationContext applicationContext) {
            return applicationContext.getBean(ReceiptCreatedHandler.class);
        }
    },

    DISCARD("discard", "已作废") {
        @Override
        public ReceiptStatusHandler getHandler(ApplicationContext applicationContext) {
            return applicationContext.getBean(ReceiptDiscardHandler.class);
        }
        @Override
        public StatusTransactionAsyncHandler<ReceiptAsyncEvent> getTransactionHandler(ApplicationContext applicationContext) {
            return applicationContext.getBean(ReceiptDiscardHandler.class);
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


    public static ReceiptStatusEnum of(String status) {
        return Arrays.stream(values()).filter(e -> e.value().equals(status)).findAny().orElse(null);
    }

    /**
     * 获取状态处理器
     * @param applicationContext
     * @return
     */
    public abstract ReceiptStatusHandler getHandler(ApplicationContext applicationContext);

    /**
     * 获取事务处理器
     * @param applicationContext
     * @return
     */
    public abstract StatusTransactionAsyncHandler<ReceiptAsyncEvent> getTransactionHandler(ApplicationContext applicationContext);

}
