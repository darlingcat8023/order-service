package com.star.enterprise.order.refund.service;

import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.service.OrderService;
import com.star.enterprise.order.core.utils.ApplicationContextUtils;
import com.star.enterprise.order.refund.data.es.entity.OrderRefundSearchInfoEntity;
import com.star.enterprise.order.refund.data.jpa.OrderRefundFeeRepository;
import com.star.enterprise.order.refund.data.jpa.OrderRefundInfoRepository;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundInfoEntity;
import com.star.enterprise.order.refund.handler.business.BusinessTypeRefundHandler;
import com.star.enterprise.order.refund.model.OrderItemDelegate;
import com.star.enterprise.order.refund.model.OrderItemObject;
import com.star.enterprise.order.refund.model.trans.OrderRefundFeeSummaryTransObject;
import com.star.enterprise.order.refund.model.trans.OrderRefundSummaryTransObject;
import com.star.enterprise.order.refund.processor.DelegatingPredicateProcessor;
import com.star.enterprise.order.refund.processor.OrderItemPredicateProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.star.enterprise.order.core.constants.OrderStatusEnum.PAID;

/**
 * @author xiaowenrou
 * @date 2023/3/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRefundService implements OrderRefundAsyncService {

    private final ApplicationContext context;

    private final OrderService orderService;

    private final OrderRefundInfoRepository infoRepository;

    private final OrderRefundFeeRepository refundFeeRepository;

    public List<OrderItemObject> findAvailableRefundOrder(TargetUser target, boolean containsMatch, boolean containsArticle) {
        return this.findAvailableOrder(target, type -> DelegatingPredicateProcessor.build(this.context, type, containsMatch, containsArticle),
                delegate -> BusinessTypeRefundHandler.createHandler(BusinessTypeEnum.of(delegate.getBusinessType()), this.context).processItemBeforePredicate(target, delegate));
    }

    public List<OrderItemObject> findAvailableOrder(TargetUser target, Function<BusinessTypeEnum, OrderItemPredicateProcessor> function, Consumer<OrderItemDelegate> consumer) {
        var list = new CopyOnWriteArrayList<OrderItemObject>();
        this.orderService.findOrder(target, PAID).parallelStream().forEach(order -> {
            var refundItemList = new ArrayList<OrderItemDelegate>();
            order.getItems().forEach(item -> function.andThen(predicate -> {
                var itemDelegate = new OrderItemDelegate(item);
                if (consumer != null) {
                    consumer.accept(itemDelegate);
                }
                if (predicate.predicate(target, itemDelegate, order)) {
                    refundItemList.add(itemDelegate);
                }
                return predicate;
            }).apply(BusinessTypeEnum.of(item.getBusinessType())));
            if (!refundItemList.isEmpty()) {
                var refund = new OrderItemObject(order, refundItemList);
                list.add(refund);
            }
        });
        return list;
    }

    @Override
    public void asyncElastic(String refundOrderId, TargetUser target, OrderRefundSearchInfoEntity entity) {
        this.refundFeeRepository.findByRefundOrderId(refundOrderId).ifPresent(fee -> entity.setDueRefundPrice(fee.getDueRefundPrice()).setFinalRefundPrice(fee.getFinalRefundPrice()));
    }

    @Override
    public void consume(Map<String, OrderRefundSummaryTransObject> objects) {
        this.refundFeeRepository.findByRefundOrderIdIn(objects.keySet()).forEach(entity -> objects.get(entity.getRefundOrderId()).setRefundFee(new OrderRefundFeeSummaryTransObject(entity)));
    }

    public Optional<OrderRefundInfoEntity> findRefundOrderInfo(final String refundOrderId) {
        return this.infoRepository.findByRefundOrderId(refundOrderId);
    }

    public OrderRefundSummaryTransObject getSummaryObject(final String refundOrderId) {
        return this.batchSummaryObject(Set.of(refundOrderId)).get(refundOrderId);
    }

    public Map<String, OrderRefundSummaryTransObject> batchSummaryObject(Set<String> refundOrderIds) {
        var map = this.infoRepository.findByRefundOrderIdIn(refundOrderIds).stream().map(OrderRefundSummaryTransObject::new).collect(Collectors.toMap(OrderRefundSummaryTransObject::getRefundOrderId, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        ApplicationContextUtils.getBeans(this.context, OrderRefundAsyncService.class).forEach(service -> service.consume(map));
        return map;
    }

}
