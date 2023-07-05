package com.star.enterprise.order.http.transfer.response;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public record DelegatingOrderTransferItemRecord(

        OrderTransferItemRecord delegate,

        BigDecimal transferNumber,

        BigDecimal transferApportion,

        BigDecimal transferPrice

) {
}
