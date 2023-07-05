package com.star.enterprise.order.transfer.calculator;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.transfer.model.OrderTransferInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
@Component
@AllArgsConstructor
public class TransferOtherFeeProcessor implements OrderTransferCalculator {

    @Override
    public void preCalculate(TargetUser target, OrderTransferInfo transferInfo, TransferAccumulateHolder holder) {
        var additional = transferInfo.additionalFee();
        holder.subtractFinalTransferPrice(additional.confirmFee())
                .subtractFinalTransferPrice(additional.manageFee())
                .subtractFinalTransferPrice(additional.cardFee())
                .subtractFinalTransferPrice(additional.dataFee())
                .subtractFinalTransferPrice(additional.offlineFee())
                .subtractFinalTransferPrice(additional.handlingFee());
    }

    @Override
    public void postCalculate(String transferOrderId, TargetUser target, OrderTransferInfo transferInfo, TransferAccumulateHolder holder) {

    }

}
