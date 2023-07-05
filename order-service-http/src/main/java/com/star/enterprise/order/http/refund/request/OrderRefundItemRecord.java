package com.star.enterprise.order.http.refund.request;

import com.star.enterprise.order.core.model.OrderBusinessInfo;
import com.star.enterprise.order.refund.model.OrderRefundItemInfo;
import com.star.enterprise.order.refund.model.RefundFeeOperator;
import com.star.enterprise.order.refund.model.RefundOrderItemContext;
import lombok.experimental.Delegate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public record OrderRefundItemRecord(

        @NotBlank(message = "receiptNo can not be null")
        String receiptNo,

        String invoiceNo,

        @NotNull(message = "purchasedDate can not be null")
        LocalDateTime purchasedDate,

        @NotBlank(message = "orderId can not be null")
        String orderId,

        @NotBlank(message = "orderItemId can not be null")
        String orderItemId,

        @NotNull(message = "refundNumber can not be null")
        BigDecimal refundNumber,

        @NotNull(message = "refundApportion can not be null")
        BigDecimal refundApportion,

        String webViewToast,

        RefundFeeOperator operator,

        @Delegate(types = {OrderBusinessInfo.class})
        RefundOrderItemContext context

) implements OrderRefundItemInfo {

    public OrderRefundItemRecord(String receiptNo, String invoiceNo, LocalDateTime purchasedDate, String orderId, String orderItemId, BigDecimal refundNumber, BigDecimal refundApportion, String webViewToast,
                                 RefundFeeOperator operator, RefundOrderItemContext context) {
        this.receiptNo = receiptNo;
        this.invoiceNo = invoiceNo;
        this.purchasedDate = purchasedDate;
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.refundNumber = refundNumber;
        this.refundApportion = refundApportion;
        this.webViewToast = webViewToast;
        this.operator = new RefundFeeOperator();
        this.context = new RefundOrderItemContext();
    }

}
