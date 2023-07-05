package com.star.enterprise.order.core.service;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.adapter.TargetUserDesAdapter;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.holder.AccumulateHolder;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.calculator.provider.CalculatorProcessorProvider;
import com.star.enterprise.order.core.constants.OrderStatusEnum;
import com.star.enterprise.order.core.data.jpa.OrderFeeRepository;
import com.star.enterprise.order.core.data.jpa.OrderInfoRepository;
import com.star.enterprise.order.core.data.jpa.entity.OrderFeeEntity;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.model.OrderCollectInfo;
import com.star.enterprise.order.core.model.OrderExtendInfo;
import com.star.enterprise.order.core.strategy.OrderIdStrategy;
import com.star.enterprise.order.core.utils.ApplicationContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.star.enterprise.order.base.exception.RestCode.CORE_ITEM_NOT_FOUND;
import static com.star.enterprise.order.base.exception.RestCode.CORE_OPERATE_NOT_ALLOW;
import static com.star.enterprise.order.charge.constants.BusinessTypeEnum.COURSE;
import static com.star.enterprise.order.core.constants.OrderSourceEnum.FRONT_END;
import static com.star.enterprise.order.core.constants.OrderStatusEnum.PAID;
import static com.star.enterprise.order.core.constants.OrderStatusEnum.TO_BE_PAID;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * 订单生命周期服务
 * @author xiaowenrou
 * @date 2022/9/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderLifecycleService {

    private final ApplicationContext applicationContext;

    private final OrderIdStrategy strategy;

    private final OrderInfoRepository orderInfoRepository;

    private final OrderFeeRepository orderFeeRepository;

    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public String saveOrderFromFrontend(AtomicReference<String> orderIdRef, TargetUser target, OrderCollectInfo info) {
        var entity = this.processOrderId(orderIdRef, target);
        var payload = info.payload();
        var holder = DelegatingAccumulateHolder.newInstance();
        var verify = ApplicationContextUtils.getBeans(this.applicationContext, OrderVerifyService.class);
        verify.forEach(service -> service.beforeCalculate(target, info));
        var chain = CalculatorProcessorProvider.supply(this.applicationContext, info.payload());
        // 执行前置计算逻辑
        chain.preCalculate(payload, chain, holder, target);
        verify.forEach(service -> service.beforeOrderSaved(orderIdRef.get(), target, info, holder));
        // 保存订单主体
        this.processOrderInfoSave(entity, target, payload, holder, TO_BE_PAID);
        chain.postCalculate(orderIdRef.get(), payload, chain, holder, target);
        verify.forEach(service -> service.afterOrderSaved(orderIdRef.get(), target, info, holder));
        return orderIdRef.get();
    }

    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public String createOrderFromFrontend(AtomicReference<String> orderIdRef, TargetUser target, OrderCollectInfo info) {
        var entity = this.processOrderId(orderIdRef, target);
        var payload = info.payload();
        var holder = DelegatingAccumulateHolder.newInstance();
        var verify = ApplicationContextUtils.getBeans(this.applicationContext, OrderVerifyService.class);
        verify.forEach(service -> service.beforeCalculate(target, info));
        var chain = CalculatorProcessorProvider.supplyWithExecute(this.applicationContext, info.payload());
        // 执行前置计算逻辑
        chain.preCalculate(payload, chain, holder, target);
        // 订单前置校验
        verify.forEach(service -> service.beforeOrderPaid(orderIdRef.get(), target, info, holder));
        // 保存订单主体
        this.processOrderInfoSave(entity, target, payload, holder, PAID);
        // 执行后置处理器
        chain.postCalculate(orderIdRef.get(), payload, chain, holder, target);
        // 触发回调钩子
        verify.forEach(service -> service.afterOrderPaid(orderIdRef.get(), target, info, holder));
        entity.addDelayTasks(holder.getDelayTasks());
        return orderIdRef.get();
    }

    private OrderInfoEntity processOrderId(AtomicReference<String> orderIdRef, TargetUser target) {
        // 生成订单id
        if (!StringUtils.hasText(orderIdRef.get())) {
            orderIdRef.set(this.strategy.generateOrderId(FRONT_END, COURSE, target));
            return new OrderInfoEntity().setOrderId(orderIdRef.get());
        } else {
            return this.orderInfoRepository.findByOrderId(orderIdRef.get()).map(order -> {
                if (PAID.value().equals(order.getStatus())) {
                    throw new BusinessWarnException(CORE_OPERATE_NOT_ALLOW, "error.order.paid");
                }
                order.setTarget(target);
                return order;
            }).orElseThrow(() -> new BusinessWarnException(CORE_ITEM_NOT_FOUND, "error.order.notFound"));
        }
    }

    private void processOrderInfoSave(OrderInfoEntity oe, TargetUser target, OrderFeeDetail payload, AccumulateHolder holder, OrderStatusEnum status) {
        oe.setOrderSource(payload.orderSource()).setStatus(status.value()).setWebViewToast(payload.webViewToast()).setTargetToast(Jackson.writeString(new TargetUserDesAdapter(target)))
                .setBusinessDate(payload.businessDate()).setTarget(target).setCompletedDate(status.equals(PAID) ? LocalDateTime.now() : null).setTarget(target);
        this.orderInfoRepository.saveAndFlush(oe);
        var orderId = oe.getOrderId();
        var of = this.orderFeeRepository.findByOrderId(orderId).orElseGet(() -> new OrderFeeEntity().setOrderId(orderId)).setOrderOriginPrice(holder.getOrderTotalPrice()).setOrderAfterDiscountPrice(holder.getOrderAfterDiscountTotalPrice())
                .setUseDirect(payload.discount().useDirectDiscount()).setUseCoupons(holder.getUseCoupons()).setUseWallet(payload.discount().walletDiscount().useWallet())
                .setOrderDueCollectPrice(holder.getOrderDueCollectPrice());
        this.orderFeeRepository.saveAndFlush(of);
    }

    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void modifyExistOrder(String orderId, OrderExtendInfo orderExtend, Map<String, MatchResult> categories) {
        Consumer<OrderInfoEntity> consumer = entity -> OrderStatusEnum.of(entity.getStatus()).getHandler(this.applicationContext).processOrderModify(orderId, entity, orderExtend, categories);
        this.orderInfoRepository.findByOrderId(orderId).ifPresent(consumer);
    }

    /**
     * 取消订单
     * @param orderIdRef
     * @return
     */
    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void cancelOrder(AtomicReference<String> orderIdRef) {
        Consumer<OrderInfoEntity> consumer = entity -> OrderStatusEnum.of(entity.getStatus()).getHandler(this.applicationContext).processOrderCancel(entity.getOrderId(), entity);
        this.orderInfoRepository.findByOrderId(orderIdRef.get()).ifPresent(consumer);
    }

    /**
     * 删除订单
     * @param orderIdRef
     */
    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void deleteOrder(AtomicReference<String> orderIdRef) {
        Consumer<OrderInfoEntity> consumer = entity -> OrderStatusEnum.of(entity.getStatus()).getHandler(this.applicationContext).processOrderDelete(entity.getOrderId(), entity);
        this.orderInfoRepository.findByOrderId(orderIdRef.get()).ifPresent(consumer);
    }

}
