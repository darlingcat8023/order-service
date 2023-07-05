package com.star.enterprise.order.receipt.constants;

import com.star.enterprise.order.base.EnumSerialize;
import com.star.enterprise.order.receipt.service.ReceiptOrderPaidService;
import com.star.enterprise.order.receipt.service.ReceiptRefundOrderService;
import com.star.enterprise.order.receipt.service.ReceiptTransferOrderService;
import com.star.enterprise.order.receipt.service.ReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * @author xiaowenrou
 * @date 2022/11/29
 */
@AllArgsConstructor
public enum ReceiptTypeEnum implements EnumSerialize {

    ORDER_PAID("order_paid", "收费") {
        @Override
        public ReceiptService getReceiptService(ApplicationContext applicationContext) {
            return applicationContext.getBean(ReceiptOrderPaidService.class);
        }
    },

    REFUND_ORDER("refund_order", "退费") {
        @Override
        public ReceiptService getReceiptService(ApplicationContext applicationContext) {
            return applicationContext.getBean(ReceiptRefundOrderService.class);
        }
    },

    TRANSFER_ORDER("transfer_order", "结转") {
        @Override
        public ReceiptService getReceiptService(ApplicationContext applicationContext) {
            return applicationContext.getBean(ReceiptTransferOrderService.class);
        }
    },

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

    public static ReceiptTypeEnum of(String value) {
        return Arrays.stream(values()).filter(e -> e.value().equals(value)).findAny().orElse(null);
    }

    /**
     * 获取对应的收据服务
     * @param applicationContext
     * @return
     */
    public abstract ReceiptService getReceiptService(ApplicationContext applicationContext);

}
