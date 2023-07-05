package com.star.enterprise.order.transfer.handler.business;

import com.star.enterprise.order.charge.model.TargetUser;
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
public class MatchBusinessTransferHandler extends BusinessTypeTransferHandler {

    private final ArticleBusinessTransferHandler delegate;

    @Override
    public void processItemAfterSaved(String transferOrderId, TargetUser target, OrderTransferItemInfo item, TransferAccumulateHolder holder) {
        this.delegate.processItemAfterSaved(transferOrderId, target, item, holder);
    }

}
