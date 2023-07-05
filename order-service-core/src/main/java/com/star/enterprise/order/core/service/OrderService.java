package com.star.enterprise.order.core.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.constants.OrderStatusEnum;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.data.jpa.OrderFeeRepository;
import com.star.enterprise.order.core.data.jpa.OrderInfoRepository;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.data.jpa.entity.QOrderInfoEntity;
import com.star.enterprise.order.core.data.jpa.entity.QOrderTargetCacheEntity;
import com.star.enterprise.order.core.model.trans.OrderFeeSummaryTransObject;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import com.star.enterprise.order.core.utils.ApplicationContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.star.enterprise.order.core.constants.OrderSourceEnum.FRONT_END;

/**
 * @author xiaowenrou
 * @date 2022/10/26
 */
@Service
@RequiredArgsConstructor
public class OrderService implements OrderAsyncService {

    private final OrderInfoRepository infoRepository;

    private final OrderFeeRepository orderFeeRepository;

    private final ApplicationContext applicationContext;

    @PersistenceContext
    private EntityManager entityManager;

    public Collection<OrderSummaryTransObject> findOrder(TargetUser target, OrderStatusEnum status) {
        return this.buildOrderQuery(target, status);
    }

    public Optional<OrderInfoEntity> findOrderInfo(final String orderId) {
        return this.infoRepository.findByOrderId(orderId);
    }

    public OrderSummaryTransObject getOrderInfoSummaryObject(final String orderId) {
        return this.batchOrderInfoSummaryObject(Set.of(orderId)).get(orderId);
    }

    public Map<String, OrderSummaryTransObject> batchOrderInfoSummaryObject(Set<String> orderIds) {
        var map = this.infoRepository.findByOrderIdIn(orderIds).stream().map(OrderSummaryTransObject::new).collect(Collectors.toMap(OrderSummaryTransObject::getOrderId, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        ApplicationContextUtils.getBeans(this.applicationContext, OrderAsyncService.class).forEach(service -> service.consume(map));
        return map;
    }

    @Override
    public void asyncElastic(String orderId, TargetUser target, OrderSearchInfoEntity entity) {
        this.orderFeeRepository.findByOrderId(orderId).ifPresent(fee -> entity.setOrderDueCollectPrice(fee.getOrderDueCollectPrice()));
    }

    @Override
    public void consume(Map<String, OrderSummaryTransObject> objects) {
        this.orderFeeRepository.findByOrderIdIn(objects.keySet()).forEach(entity -> objects.get(entity.getOrderId()).setOrderFee(new OrderFeeSummaryTransObject(entity)));
    }

    private Collection<OrderSummaryTransObject> buildOrderQuery(TargetUser target, OrderStatusEnum status) {
        var query = new JPAQuery<>(this.entityManager);
        var oi = QOrderInfoEntity.orderInfoEntity;
        var otc= QOrderTargetCacheEntity.orderTargetCacheEntity;
        var list = query.select(oi).from(oi).leftJoin(otc).on(oi.orderId.eq(otc.orderId))
                .where(oi.orderSource.eq(FRONT_END.value()), oi.status.eq(status.value()), otc.targetId.eq(target.targetId()), otc.targetCampusId.eq(target.campus()))
                .fetch();
        var map = list.stream().map(OrderSummaryTransObject::new).collect(Collectors.toMap(OrderSummaryTransObject::getOrderId, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        ApplicationContextUtils.getBeans(this.applicationContext, OrderAsyncService.class).forEach(service -> service.consume(map));
        return map.values();
    }

}
