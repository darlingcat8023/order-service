package com.star.enterprise.order.http.transfer.response;

import com.star.enterprise.order.refund.model.RefundAdditionalFee;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public record OrderTransferCalculateResponse(

        List<DelegatingOrderTransferItemRecord> delegatingRecords,

        RefundAdditionalFee additionalFee,

        OrderTransferFeeRecord refundFeeRecord

) {
}
