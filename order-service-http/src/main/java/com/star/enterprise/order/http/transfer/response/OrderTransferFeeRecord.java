package com.star.enterprise.order.http.transfer.response;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public record OrderTransferFeeRecord(

        BigDecimal totalOverPrice,

        BigDecimal dueTransferPrice

) {
}
