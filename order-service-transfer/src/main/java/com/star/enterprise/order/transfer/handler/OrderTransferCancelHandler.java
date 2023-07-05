package com.star.enterprise.order.transfer.handler;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.core.handler.order.StatusTransactionAsyncHandler;
import com.star.enterprise.order.message.sender.TransferOrderStatusMessageSender;
import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferInfoEntity;
import com.star.enterprise.order.transfer.event.OrderTransferInfoAsyncEvent;
import com.star.enterprise.order.transfer.model.TransferExtendInfo;
import com.star.enterprise.order.transfer.service.OrderTransferElasticsearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.star.enterprise.order.base.exception.RestCode.TRANSFER_ITEM_NOT_ALLOW;

/**
 * @author xiaowenrou
 * @date 2023/3/14
 */
@Component
@AllArgsConstructor
public class OrderTransferCancelHandler implements OrderTransferStatusHandler, StatusTransactionAsyncHandler<OrderTransferInfoAsyncEvent> {

    private final TransferOrderStatusMessageSender messageSender;

    private final OrderTransferElasticsearchService elasticsearchService;


    @Override
    public void onCurrentStatusCommitTransactionAsync(OrderTransferInfoAsyncEvent event) {
        this.messageSender.sendMessage(event.transferOrderId(), event);
        this.elasticsearchService.cancelElasticTransferOrder(event.transferOrderId());
    }

    @Override
    public void processOrderTransferModify(String refundOrderId, TransferExtendInfo extendInfo, OrderTransferInfoEntity entity) {
        throw new BusinessWarnException(TRANSFER_ITEM_NOT_ALLOW, "error.transfer.canceled");
    }

    @Override
    public void processOrderTransferCancel(String transferOrderId, OrderTransferInfoEntity entity) {
        throw new BusinessWarnException(TRANSFER_ITEM_NOT_ALLOW, "error.transfer.canceled");
    }

}
