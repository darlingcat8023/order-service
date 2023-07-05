package com.star.enterprise.order.transfer.handler.business;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculatorDelayTask;
import com.star.enterprise.order.remote.wallet.RemoteWalletCourseService;
import com.star.enterprise.order.transfer.calculator.TransferAccumulateHolder;
import com.star.enterprise.order.transfer.model.OrderTransferItemInfo;
import com.star.enterprise.wallet.api.request.WalletCourseTransferRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 对于课程类型的业务处理器
 * @author xiaowenrou
 * @date 2022/11/4
 */
@Slf4j
@Component
@AllArgsConstructor
public class CourseBusinessTransferHandler extends BusinessTypeTransferHandler {

    private final RemoteWalletCourseService walletCourseService;

    @Override
    public void processItemAfterSaved(String refundOrderId, TargetUser target, OrderTransferItemInfo item, TransferAccumulateHolder holder) {
        CalculatorDelayTask task = refundId -> {
            var req = new WalletCourseTransferRecord(item.orderId(), item.orderItemId(), "", refundId, item.context().getRefundOrderItemId(), item.transferNumber(), item.transferApportion());
            this.walletCourseService.createTransfer(target.targetId(), target.campus(), req);
        };
        holder.addDelayTask(task);
        super.processItemAfterSaved(refundOrderId, target, item, holder);
    }

}
