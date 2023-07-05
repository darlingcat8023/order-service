package com.star.enterprise.order.http.refund.response;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public record DelegatingOrderRefundItemRecord(

        OrderRefundItemRecord delegate,

        BigDecimal refundNumber,

        BigDecimal refundApportion,

        BigDecimal refundPrice,

        BigDecimal refundOverPrice

) {
}
