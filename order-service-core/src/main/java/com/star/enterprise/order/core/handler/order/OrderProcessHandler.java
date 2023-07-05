package com.star.enterprise.order.core.handler.order;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.event.OrderInfoAsyncEvent;
import com.star.enterprise.order.core.model.OrderExtendInfo;
import com.star.enterprise.order.message.base.IOrderStatusMessage;
import com.star.enterprise.order.message.sender.OrderStatusMessageSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.star.enterprise.order.base.exception.RestCode.CORE_OPERATE_NOT_ALLOW;
import static com.star.enterprise.order.core.constants.OrderStatusEnum.CANCEL;

/**
 * @author xiaowenrou
 * @date 2023/3/24
 */
@Slf4j
@Component
@AllArgsConstructor
public class OrderProcessHandler implements OrderStatusHandler, StatusTransactionAsyncHandler<OrderInfoAsyncEvent> {

    private final OrderSaveHandler delegate;


    private final OrderStatusMessageSender messageSender;

    @Override
    public void onCurrentStatusCommitTransactionAsync(OrderInfoAsyncEvent event) {
        this.delegate.onCurrentStatusCommitTransactionAsync(event);
    }

    @Override
    public void onCurrentStatusRollbackTransactionAsync(OrderInfoAsyncEvent event) {
        this.messageSender.sendMessage(event.orderId(), new IOrderStatusMessage() {
            @Override
            public String orderId() {return event.orderId();}

            @Override
            public String status() {return CANCEL.value();}

            @Override
            public String targetId() {return event.targetId();}

            @Override
            public String campusId() {return event.campusId();}
        });
    }

    @Override
    public void processOrderModify(String orderId, OrderInfoEntity entity, OrderExtendInfo extendInfo, Map<String, MatchResult> categories) {
        throw new BusinessWarnException(CORE_OPERATE_NOT_ALLOW, "error.order.deleted");
    }

    @Override
    public void processOrderCancel(String orderId, OrderInfoEntity entity) {
        throw new BusinessWarnException(CORE_OPERATE_NOT_ALLOW, "error.order.deleted");
    }

}
