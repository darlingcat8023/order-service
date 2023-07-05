package com.star.enterprise.order.refund.handler.business;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.data.jpa.entity.OrderItemLeftEntity;
import com.star.enterprise.order.core.service.OrderItemService;
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
public class ArticleBusinessRefundHandler extends BusinessTypeRefundHandler {

    private final OrderItemService orderItemService;

    @Override
    public void processItemAfterSaved(String refundOrderId, TargetUser targetUser, OrderRefundItemInfo item, RefundAccumulateHolder holder) {
        var delegate = item.context().getDelegate();
        var ent = new OrderItemLeftEntity().setOrderItemId(item.orderItemId()).setVersion(delegate.getVersion())
                .setNumberLeft(delegate.getNumberLeft().subtract(item.refundNumber())).setApportionLeft(delegate.getApportionLeft().subtract(item.refundApportion()));
        this.orderItemService.modifyOrderItemLeft(ent);
        super.processItemAfterSaved(refundOrderId, targetUser, item, holder);
    }

}
