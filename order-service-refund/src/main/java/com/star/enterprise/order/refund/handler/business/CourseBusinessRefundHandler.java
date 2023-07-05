package com.star.enterprise.order.refund.handler.business;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculatorDelayTask;
import com.star.enterprise.order.refund.calculator.RefundAccumulateHolder;
import com.star.enterprise.order.refund.model.OrderRefundItemInfo;
import com.star.enterprise.order.remote.wallet.RemoteWalletCourseService;
import com.star.enterprise.wallet.api.request.WalletCourseRefundRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 对于课程类型的业务处理器
 * @author xiaowenrou
 * @date 2022/11/4
 */
@Slf4j
@Component
@AllArgsConstructor
public class CourseBusinessRefundHandler extends BusinessTypeRefundHandler {

    private final RemoteWalletCourseService walletCourseService;

    @Override
    public void processItemBusinessInfo(TargetUser targetUser, OrderRefundItemInfo item, RefundAccumulateHolder holder) {
        super.processItemBusinessInfo(targetUser, item, holder);
        this.calculateOverPrice(item, holder);
    }

    private void calculateOverPrice(OrderRefundItemInfo item, RefundAccumulateHolder holder) {
        // 课程类型处理退费溢价
        var delegate = item.context().getDelegate();
        var opt = item.operator();
        var overNumber = new BigDecimal(delegate.getNumber()).subtract(item.refundNumber());
        var overPrice = delegate.getOriginPrice().subtract(delegate.getItemFee().getDueCollectSinglePrice());
        opt.setRefundOverPrice(overNumber.multiply(overPrice));
        holder.addOverPrice(opt.getRefundOverPrice());
        holder.subtractDueRefundPrice(opt.getRefundOverPrice());
    }

    @Override
    public void processItemAfterSaved(String refundOrderId, TargetUser targetUser, OrderRefundItemInfo item, RefundAccumulateHolder holder) {
        CalculatorDelayTask task = refundId -> {
            var req = new WalletCourseRefundRecord(item.orderId(), item.orderItemId(), "", refundId, item.context().getRefundOrderItemId(), item.refundNumber(), item.refundApportion());
            this.walletCourseService.createRefund(targetUser.targetId(), targetUser.campus(), req);
        };
        holder.addDelayTask(task);
        super.processItemAfterSaved(refundOrderId, targetUser, item, holder);
    }


}
