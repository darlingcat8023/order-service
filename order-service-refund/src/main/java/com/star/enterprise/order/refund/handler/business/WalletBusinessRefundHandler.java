package com.star.enterprise.order.refund.handler.business;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.refund.model.OrderItemDelegate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/4/19
 */
@Component
@AllArgsConstructor
public class WalletBusinessRefundHandler extends BusinessTypeRefundHandler {

    @Override
    public void processItemBeforePredicate(TargetUser targetUser, OrderItemDelegate delegate) {
        delegate.setNumberLeft(BigDecimal.ZERO);
        delegate.setApportionLeft(BigDecimal.ZERO);
        super.processItemBeforePredicate(targetUser, delegate);
    }

}
