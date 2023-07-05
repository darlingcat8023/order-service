package com.star.enterprise.order.transfer.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.utils.ApplicationContextUtils;
import com.star.enterprise.order.refund.model.OrderItemObject;
import com.star.enterprise.order.refund.service.OrderRefundService;
import com.star.enterprise.order.transfer.data.es.entity.OrderTransferSearchInfoEntity;
import com.star.enterprise.order.transfer.data.jpa.OrderTransferFeeRepository;
import com.star.enterprise.order.transfer.data.jpa.OrderTransferInfoRepository;
import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferInfoEntity;
import com.star.enterprise.order.transfer.model.trans.OrderTransferFeeSummaryTransObject;
import com.star.enterprise.order.transfer.model.trans.OrderTransferSummaryTransObject;
import com.star.enterprise.order.transfer.processor.DelegatingTransferPredicateProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xiaowenrou
 * @date 2023/3/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderTransferService implements OrderTransferAsyncService {

    private final OrderRefundService delegate;

    private final ApplicationContext context;

    private final OrderTransferInfoRepository infoRepository;

    private final OrderTransferFeeRepository transferFeeRepository;

    public List<OrderItemObject> findAvailableTransferOrder(TargetUser target, boolean containsMatch, boolean containsArticle) {
        return this.delegate.findAvailableOrder(target, type -> DelegatingTransferPredicateProcessor.build(this.context, type, containsMatch, containsArticle), null);
    }

    @Override
    public void asyncElastic(String transferOrderId, TargetUser target, OrderTransferSearchInfoEntity entity) {
        this.transferFeeRepository.findByTransferOrderId(transferOrderId).ifPresent(fee -> entity.setDueTransferPrice(fee.getDueTransferPrice()).setFinalTransferPrice(fee.getFinalTransferPrice()));
    }

    @Override
    public void consume(Map<String, OrderTransferSummaryTransObject> objects) {
        this.transferFeeRepository.findByTransferOrderIdIn(objects.keySet()).forEach(entity -> objects.get(entity.getTransferOrderId()).setTransferFee(new OrderTransferFeeSummaryTransObject(entity)));
    }

    public Optional<OrderTransferInfoEntity> findTransferOrderInfo(final String transferOrderId) {
        return this.infoRepository.findByTransferOrderId(transferOrderId);
    }

    public OrderTransferSummaryTransObject getSummaryObject(final String transferOrderId) {
        return this.batchSummaryObject(Set.of(transferOrderId)).get(transferOrderId);
    }

    public Map<String, OrderTransferSummaryTransObject> batchSummaryObject(Set<String> transferOrderIds) {
        var map = this.infoRepository.findByTransferOrderIdIn(transferOrderIds).stream().map(OrderTransferSummaryTransObject::new).collect(Collectors.toMap(OrderTransferSummaryTransObject::getTransferOrderId, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        ApplicationContextUtils.getBeans(this.context, OrderTransferAsyncService.class).forEach(service -> service.consume(map));
        return map;
    }

}
