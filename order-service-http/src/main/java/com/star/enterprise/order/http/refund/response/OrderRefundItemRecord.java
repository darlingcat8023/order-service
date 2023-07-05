package com.star.enterprise.order.http.refund.response;

import com.star.enterprise.order.core.model.trans.OrderItemSummaryTransObject;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xiaowenrou
 * @date 2023/3/2
 */
public record OrderRefundItemRecord(

        String receiptNo,

        String orderId,

        String invoiceNo,

        LocalDateTime purchasedDate,

        String orderItemId,

        String businessId,

        String businessType,

        String productName,

        BigDecimal number,

        BigDecimal apportion,

        BigDecimal numberLeft,

        BigDecimal apportionLeft,

        BigDecimal balance,

        BigDecimal originSinglePrice,

        BigDecimal dueCollectSinglePrice,

        BigDecimal currentStandardPrice,

        OrderItemSummaryTransObject origin

) { }
