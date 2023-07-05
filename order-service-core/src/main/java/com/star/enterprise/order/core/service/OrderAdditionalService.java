package com.star.enterprise.order.core.service;

import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.holder.AccumulateHolder;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.data.jpa.OrderAdditionalRepository;
import com.star.enterprise.order.core.data.jpa.OrderPerformanceRepository;
import com.star.enterprise.order.core.data.jpa.entity.OrderAdditionalEntity;
import com.star.enterprise.order.core.data.jpa.entity.OrderPerformanceEntity;
import com.star.enterprise.order.core.model.AdditionalInformation;
import com.star.enterprise.order.core.model.OrderCollectInfo;
import com.star.enterprise.order.core.model.PerformanceUser;
import com.star.enterprise.order.core.model.trans.OrderAdditionalSummaryTransObject;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import lombok.AllArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * 订单发票服务
 * @author xiaowenrou
 * @date 2022/9/22
 */
@Service
@AllArgsConstructor
public class OrderAdditionalService implements OrderAsyncService, OrderVerifyService {

    private final OrderAdditionalRepository additionalRepository;

    private final OrderPerformanceRepository performanceRepository;

    @Override
    public void afterOrderSaved(String orderId, TargetUser target, OrderCollectInfo info, AccumulateHolder holder) {
        var service = (OrderAdditionalService) AopContext.currentProxy();
        service.saveInvoice(orderId, info.invoice());
        OrderVerifyService.super.afterOrderSaved(orderId, target, info, holder);
    }

    @Override
    public void afterOrderPaid(String orderId, TargetUser target, OrderCollectInfo info, AccumulateHolder holder) {
        this.afterOrderSaved(orderId, target, info, holder);
    }

    /**
     * 保存发票信息
     * @param orderId
     * @param invoiceInfo
     */
    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void saveInvoice(final String orderId, AdditionalInformation invoiceInfo) {
        if (invoiceInfo == null) {
            return;
        }
        // 保存业绩人
        this.performanceRepository.deleteByOrderId(orderId);
        if (!CollectionUtils.isEmpty(invoiceInfo.performanceUser())) {
            Function<PerformanceUser, OrderPerformanceEntity> pFunction = user -> new OrderPerformanceEntity()
                    .setOrderId(orderId).setUserId(user.getUserId()).setUserName(user.getUserName()).setUserRole(user.getUserRole());
            Predicate<PerformanceUser> predicate = item -> StringUtils.hasText(item.getUserId()) && StringUtils.hasText(item.getUserName());
            this.performanceRepository.saveAllAndFlush(invoiceInfo.performanceUser().stream().filter(predicate).map(pFunction).toList());
        }
        this.additionalRepository.deleteByOrderId(orderId);
        var invoice = new OrderAdditionalEntity().setOrderId(orderId).setInvoiceNo(invoiceInfo.invoiceNo()).setInvoiceAmount(invoiceInfo.invoiceAmount())
                .setCerts(Jackson.writeString(Objects.requireNonNullElseGet(invoiceInfo.certs(), List::of)))
                .setInnerRemark(invoiceInfo.innerRemark()).setOuterRemark(invoiceInfo.outerRemark());
        this.additionalRepository.saveAndFlush(invoice);
    }

    public OrderAdditionalSummaryTransObject getOrderInvoiceInfo(final String orderId) {
        var ip = this.performanceRepository.findByOrderId(orderId).stream().map(usr -> new PerformanceUser(usr.getUserId(), usr.getUserName(), usr.getUserRole())).toList();
        var object = new OrderAdditionalSummaryTransObject().setPerformanceUser(ip);
        this.additionalRepository.findByOrderId(orderId).ifPresent(entity -> object.setInvoiceNo(entity.getInvoiceNo()).setInvoiceAmount(entity.getInvoiceAmount())
                .setCerts(entity.getCertsList()).setOuterRemark(entity.getOuterRemark()).setInnerRemark(entity.getInnerRemark()));
        return object;
    }

    @Override
    public void asyncElastic(String orderId, TargetUser target, OrderSearchInfoEntity entity) {
        var ip = this.performanceRepository.findByOrderId(orderId).stream().map(usr -> new PerformanceUser(usr.getUserId(), usr.getUserName(), usr.getUserRole())).toList();
        entity.setPerformanceUsers(ip);
        var add = entity.getAdditional();
        this.additionalRepository.findByOrderId(orderId).ifPresent(orderAdditional -> {
            add.put("innerRemark", orderAdditional.getInnerRemark());
            add.put("outerRemark", orderAdditional.getOuterRemark());
            add.put("invoiceNo", orderAdditional.getInvoiceNo());
        });
    }

    @Override
    public void consume(Map<String, OrderSummaryTransObject> objects) {
        var addMap = this.additionalRepository.findByOrderIdIn(objects.keySet()).stream().collect(Collectors.toMap(OrderAdditionalEntity::getOrderId, Function.identity()));
        Function<OrderPerformanceEntity, PerformanceUser> function = usr -> new PerformanceUser(usr.getUserId(), usr.getUserName(), usr.getUserRole());
        var perMap = this.performanceRepository.findByOrderIdIn(objects.keySet()).stream().collect(Collectors.groupingBy(OrderPerformanceEntity::getOrderId, Collectors.mapping(function, Collectors.toList())));
        objects.forEach((orderId, summary) -> {
            var trans = new OrderAdditionalSummaryTransObject();
            if (perMap.containsKey(orderId)) {
                trans.setPerformanceUser(perMap.get(orderId));
            }
            if (addMap.containsKey(orderId)) {
                var entity = addMap.get(orderId);
                trans.setInvoiceNo(entity.getInvoiceNo()).setInvoiceAmount(entity.getInvoiceAmount()).setCerts(entity.getCertsList()).setOuterRemark(entity.getOuterRemark()).setInnerRemark(entity.getInnerRemark());
            }
            summary.setAdditional(trans);
        });
    }

}
