package com.star.enterprise.order.transfer.handler.business;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.data.jpa.entity.OrderItemLeftEntity;
import com.star.enterprise.order.core.service.OrderItemService;
import com.star.enterprise.order.transfer.calculator.TransferAccumulateHolder;
import com.star.enterprise.order.transfer.model.OrderTransferItemInfo;
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
public class ArticleBusinessTransferHandler extends BusinessTypeTransferHandler {

    private final OrderItemService orderItemService;

    @Override
    public void processItemAfterSaved(String transferOrderId, TargetUser target, OrderTransferItemInfo item, TransferAccumulateHolder holder) {
        var delegate = item.context().getDelegate();
        var ent = new OrderItemLeftEntity().setOrderItemId(item.orderItemId()).setVersion(delegate.getVersion())
                .setNumberLeft(delegate.getNumberLeft().subtract(item.transferNumber())).setApportionLeft(delegate.getApportionLeft().subtract(item.transferApportion()));
        this.orderItemService.modifyOrderItemLeft(ent);
        super.processItemAfterSaved(transferOrderId, target, item, holder);
    }

}
