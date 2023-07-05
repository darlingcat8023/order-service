package com.star.enterprise.order.http.transfer.request;

import com.star.enterprise.order.core.model.OrderBusinessInfo;
import com.star.enterprise.order.transfer.model.OrderTransferItemInfo;
import com.star.enterprise.order.transfer.model.TransferFeeOperator;
import com.star.enterprise.order.transfer.model.TransferOrderItemContext;
import lombok.experimental.Delegate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public record OrderTransferItemRecord(

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
        BigDecimal transferNumber,

        @NotNull(message = "refundApportion can not be null")
        BigDecimal transferApportion,

        String webViewToast,

        TransferFeeOperator operator,

        @Delegate(types = {OrderBusinessInfo.class})
        TransferOrderItemContext context

) implements OrderTransferItemInfo {

    public OrderTransferItemRecord(String receiptNo, String invoiceNo, LocalDateTime purchasedDate, String orderId, String orderItemId, BigDecimal transferNumber, BigDecimal transferApportion, String webViewToast,
                                   TransferFeeOperator operator, TransferOrderItemContext context) {
        this.receiptNo = receiptNo;
        this.invoiceNo = invoiceNo;
        this.purchasedDate = purchasedDate;
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.transferNumber = transferNumber;
        this.transferApportion = transferApportion;
        this.webViewToast = webViewToast;
        this.operator = new TransferFeeOperator();
        this.context = new TransferOrderItemContext();
    }

}
