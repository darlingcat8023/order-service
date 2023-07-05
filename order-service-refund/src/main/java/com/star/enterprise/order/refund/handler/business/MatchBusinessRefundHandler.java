package com.star.enterprise.order.refund.handler.business;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.refund.calculator.RefundAccumulateHolder;
import com.star.enterprise.order.refund.model.OrderRefundItemInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xiaowenrou
 * @date 2022/11/4
 */
@Slf4j
@Component
@AllArgsConstructor
public class MatchBusinessRefundHandler extends BusinessTypeRefundHandler {

    private final ArticleBusinessRefundHandler delegate;

    @Override
    public void processItemAfterSaved(String refundOrderId, TargetUser targetUser, OrderRefundItemInfo item, RefundAccumulateHolder holder) {
        this.delegate.processItemAfterSaved(refundOrderId, targetUser, item, holder);
    }

}
