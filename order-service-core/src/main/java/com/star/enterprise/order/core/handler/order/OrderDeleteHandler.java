package com.star.enterprise.order.core.handler.order;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.core.data.es.OrderSearchInfoRepository;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.event.OrderInfoAsyncEvent;
import com.star.enterprise.order.core.model.OrderExtendInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.star.enterprise.order.base.exception.RestCode.CORE_OPERATE_NOT_ALLOW;

/**
 * @author xiaowenrou
 * @date 2022/11/3
 */
@Component
@AllArgsConstructor
public class OrderDeleteHandler implements OrderStatusHandler, StatusTransactionAsyncHandler<OrderInfoAsyncEvent> {

    private final OrderSearchInfoRepository searchInfoRepository;

    @Override
    public void onCurrentStatusCommitTransactionAsync(final OrderInfoAsyncEvent event) {
        this.searchInfoRepository.deleteByOrderId(event.orderId());
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
