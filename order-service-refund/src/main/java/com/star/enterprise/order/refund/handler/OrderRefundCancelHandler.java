package com.star.enterprise.order.refund.handler;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.core.handler.order.StatusTransactionAsyncHandler;
import com.star.enterprise.order.message.sender.RefundOrderStatusMessageSender;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundInfoEntity;
import com.star.enterprise.order.refund.event.OrderRefundInfoAsyncEvent;
import com.star.enterprise.order.refund.model.RefundExtendInfo;
import com.star.enterprise.order.refund.service.OrderRefundElasticsearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.star.enterprise.order.base.exception.RestCode.REFUND_ITEM_NOT_ALLOW;

/**
 * @author xiaowenrou
 * @date 2023/3/14
 */
@Component
@AllArgsConstructor
public class OrderRefundCancelHandler implements OrderRefundStatusHandler, StatusTransactionAsyncHandler<OrderRefundInfoAsyncEvent> {

    private final RefundOrderStatusMessageSender messageSender;

    private final OrderRefundElasticsearchService elasticsearchService;

    @Override
    public void onCurrentStatusCommitTransactionAsync(OrderRefundInfoAsyncEvent event) {
        this.messageSender.sendMessage(event.refundOrderId(), event);
        this.elasticsearchService.cancelElasticRefundOrder(event.refundOrderId());
    }

    @Override
    public void processOrderRefundModify(String refundOrderId, RefundExtendInfo extendInfo, OrderRefundInfoEntity entity) {
        throw new BusinessWarnException(REFUND_ITEM_NOT_ALLOW, "error.refund.canceled");
    }

    @Override
    public void processOrderRefundCancel(String refundOrderId, OrderRefundInfoEntity entity) {
        throw new BusinessWarnException(REFUND_ITEM_NOT_ALLOW, "error.refund.canceled");
    }

}
