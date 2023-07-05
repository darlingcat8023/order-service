package com.star.enterprise.order.refund.calculator;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.refund.model.OrderRefundInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
@Component
@AllArgsConstructor
public class RefundOtherFeeProcessor implements OrderRefundCalculator {

    @Override
    public void preCalculate(TargetUser target, OrderRefundInfo refundInfo, RefundAccumulateHolder holder) {
        var additional = refundInfo.additionalFee();
        holder.subtractFinalRefundPrice(additional.confirmFee())
                .subtractFinalRefundPrice(additional.manageFee())
                .subtractFinalRefundPrice(additional.cardFee())
                .subtractFinalRefundPrice(additional.dataFee())
                .subtractFinalRefundPrice(additional.offlineFee())
                .subtractFinalRefundPrice(additional.handlingFee());
    }

    @Override
    public void postCalculate(String refundOrderId, TargetUser target, OrderRefundInfo refundInfo, RefundAccumulateHolder holder) {

    }

}
