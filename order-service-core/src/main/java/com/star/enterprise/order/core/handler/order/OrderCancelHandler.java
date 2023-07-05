package com.star.enterprise.order.core.handler.order;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.event.OrderInfoAsyncEvent;
import com.star.enterprise.order.core.model.OrderExtendInfo;
import com.star.enterprise.order.core.service.OrderElasticsearchService;
import com.star.enterprise.order.message.sender.OrderStatusMessageSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.star.enterprise.order.base.exception.RestCode.CORE_OPERATE_NOT_ALLOW;
import static com.star.enterprise.order.core.constants.OrderStatusEnum.CANCEL;

/**
 * @author xiaowenrou
 * @date 2022/11/3
 */
@Component
@AllArgsConstructor
public class OrderCancelHandler implements OrderStatusHandler, StatusTransactionAsyncHandler<OrderInfoAsyncEvent> {

    private final OrderElasticsearchService orderElasticsearchService;


    private final OrderStatusMessageSender messageSender;

    @Override
    public void onCurrentStatusCommitTransactionAsync(final OrderInfoAsyncEvent event) {
        this.messageSender.sendMessage(event.orderId(), event);
        this.orderElasticsearchService.modifyElasticOrderStatus(event.orderId(), CANCEL);
    }

    @Override
    public void processOrderModify(String orderId, OrderInfoEntity entity, OrderExtendInfo extendInfo, Map<String, MatchResult> categories) {
        throw new BusinessWarnException(CORE_OPERATE_NOT_ALLOW, "error.order.canceled");
    }

    @Override
    public void processOrderCancel(String orderId, OrderInfoEntity entity) {
        throw new BusinessWarnException(CORE_OPERATE_NOT_ALLOW, "error.order.canceled");
    }

}
