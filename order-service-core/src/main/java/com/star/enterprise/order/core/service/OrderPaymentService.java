package com.star.enterprise.order.core.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.holder.AccumulateHolder;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.data.es.entity.SearchedPaymentInfo;
import com.star.enterprise.order.core.data.jpa.OrderPaymentInfoRepository;
import com.star.enterprise.order.core.data.jpa.entity.OrderPaymentInfoEntity;
import com.star.enterprise.order.core.data.jpa.entity.QOrderInfoEntity;
import com.star.enterprise.order.core.data.jpa.entity.QOrderPaymentInfoEntity;
import com.star.enterprise.order.core.model.OrderCollectInfo;
import com.star.enterprise.order.core.model.PaymentInfo;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.star.enterprise.order.base.exception.RestCode.ARGUMENT_NOT_VALID;
import static com.star.enterprise.order.core.constants.OrderStatusEnum.PAID;
import static java.math.BigDecimal.ZERO;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * @author xiaowenrou
 * @date 2022/10/25
 */
@Service
@RequiredArgsConstructor
public class OrderPaymentService implements OrderAsyncService, OrderVerifyService {

    private final OrderPaymentInfoRepository paymentInfoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void afterOrderSaved(String orderId, TargetUser target, OrderCollectInfo info, AccumulateHolder holder) {
        var service = (OrderPaymentService) AopContext.currentProxy();
        service.savePayments(orderId, info.payments());
        OrderVerifyService.super.afterOrderSaved(orderId, target, info, holder);
    }

    @Override
    public void beforeOrderPaid(String orderId, TargetUser target, OrderCollectInfo info, AccumulateHolder holder) {
        var total = new BigDecimal(0);
        for (var pay : info.payments()) {
            total = total.add(pay.getPaymentAmount());
        }
        if (holder.getOrderDueCollectPrice().subtract(total).compareTo(ZERO) != 0) {
            throw new BusinessWarnException(ARGUMENT_NOT_VALID, "error.order.paymentVerifyFail");
        }
        OrderVerifyService.super.beforeOrderPaid(orderId, target, info, holder);
    }

    @Override
    public void afterOrderPaid(String orderId, TargetUser target, OrderCollectInfo info, AccumulateHolder holder) {
        this.afterOrderSaved(orderId, target, info, holder);
    }

    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void savePayments(final String orderId, List<PaymentInfo> payments) {
        this.paymentInfoRepository.deleteByOrderId(orderId);
        Function<PaymentInfo, OrderPaymentInfoEntity> function = pay -> new OrderPaymentInfoEntity().setOrderId(orderId)
                .setPaymentAccount(pay.getPaymentAccount()).setPaymentMethod(pay.getPaymentMethod()).setPaymentAmount(pay.getPaymentAmount());
        this.paymentInfoRepository.saveAllAndFlush(payments.stream().map(function).toList());
    }

    public List<PaymentInfo> getPayments(final String orderId) {
        return this.paymentInfoRepository.findByOrderId(orderId);
    }

    public <V> List<V> paymentAggregation(Function<Tuple, V> function) {
        var query = new JPAQuery<>(this.entityManager);
        var opi = QOrderPaymentInfoEntity.orderPaymentInfoEntity;
        var oi = QOrderInfoEntity.orderInfoEntity;
        // 构建子查询
        var subQuery = JPAExpressions.select(oi.orderId).from(oi).where(oi.status.eq(PAID.value()));
        var tuples = query.select(opi.paymentMethod, opi.paymentAmount.sum()).from(opi).where(opi.orderId.in(subQuery)).groupBy(opi.paymentMethod).fetch();
        return tuples.stream().map(function).toList();
    }

    @Override
    public void asyncElastic(String orderId, TargetUser target, OrderSearchInfoEntity entity) {
        var pay = this.paymentInfoRepository.findByOrderId(orderId).stream().map(p -> new SearchedPaymentInfo(p.getPaymentMethod(), p.getPaymentAccount(), p.getPaymentAmount())).toList();
        entity.setPayments(pay);
    }

    @Override
    public void consume(Map<String, OrderSummaryTransObject> objects) {
        var map = this.paymentInfoRepository.findByOrderIdIn(objects.keySet()).stream().collect(Collectors.groupingBy(PaymentInfo::getOrderId));
        objects.keySet().stream().filter(map::containsKey).forEach(key -> objects.get(key).setPayments(map.get(key)));
    }

}
