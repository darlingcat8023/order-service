package com.star.enterprise.order.http.refund.response;

import com.star.enterprise.order.http.order.response.WalletDiscountRecord;
import com.star.enterprise.order.refund.model.RefundAdditionalFee;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public record OrderRefundCalculateResponse(

        List<DelegatingOrderRefundItemRecord> delegatingRecords,

        RefundAdditionalFee additionalFee,

        WalletDiscountRecord walletRefund,

        OrderRefundFeeRecord refundFeeRecord

) {
}
