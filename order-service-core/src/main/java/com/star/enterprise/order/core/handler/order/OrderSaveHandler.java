package com.star.enterprise.order.core.handler.order;

import com.star.enterprise.order.base.exception.TaskExecuteWarnException;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.core.data.jpa.OrderInfoRepository;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.event.OrderInfoAsyncEvent;
import com.star.enterprise.order.core.model.OrderExtendInfo;
import com.star.enterprise.order.core.service.OrderSearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

import static com.star.enterprise.order.core.constants.OrderStatusEnum.CANCEL;
import static com.star.enterprise.order.core.constants.OrderStatusEnum.DELETE;

/**
 * @author xiaowenrou
 * @date 2022/11/3
 */
@Slf4j
@Component
@AllArgsConstructor
public class OrderSaveHandler implements OrderStatusHandler, StatusTransactionAsyncHandler<OrderInfoAsyncEvent> {

    private final OrderInfoRepository orderInfoRepository;

    private final OrderSearchService orderSearchService;

    @Override
    public void onCurrentStatusCommitTransactionAsync(final OrderInfoAsyncEvent event) {
        var order = this.orderInfoRepository.findByOrderId(event.orderId()).filter(o -> event.version().equals(o.getVersion()))
                .orElseThrow(() -> new TaskExecuteWarnException("order sync es fail : version not match"));
        this.orderSearchService.refresh(event.orderId(), event.target(), order);
    }

    @Override
    public void processOrderModify(String orderId, OrderInfoEntity entity, OrderExtendInfo extendInfo, Map<String, MatchResult> categories) {
        // 未支付的订单可以不用处理
    }

    @Override
    public void processOrderCancel(String orderId, OrderInfoEntity entity) {
        this.orderInfoRepository.saveAndFlush(entity.setStatus(CANCEL.value()));
    }

    @Override
    public void processOrderDelete(String orderId, OrderInfoEntity entity) {
        this.orderInfoRepository.saveAndFlush(entity.setStatus(DELETE.value()).setDeletedAt(LocalDateTime.now()));
    }

}
