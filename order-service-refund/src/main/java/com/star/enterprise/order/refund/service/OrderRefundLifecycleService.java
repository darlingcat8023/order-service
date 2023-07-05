package com.star.enterprise.order.refund.service;

import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.adapter.TargetUserDesAdapter;
import com.star.enterprise.order.refund.calculator.OrderRefundCalculatorChain;
import com.star.enterprise.order.refund.calculator.RefundAccumulateHolder;
import com.star.enterprise.order.refund.constants.OrderRefundStatusEnum;
import com.star.enterprise.order.refund.data.jpa.OrderRefundFeeRepository;
import com.star.enterprise.order.refund.data.jpa.OrderRefundInfoRepository;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundFeeEntity;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundInfoEntity;
import com.star.enterprise.order.refund.model.OrderRefundExtendInfo;
import com.star.enterprise.order.refund.model.RefundExtendInfo;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
@Service
@AllArgsConstructor
public class OrderRefundLifecycleService {

    private final ApplicationContext context;

    private final OrderRefundInfoRepository refundInfoRepository;

    private final OrderRefundFeeRepository refundFeeRepository;

    private final OrderRefundAdditionalService additionalService;

    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public String createRefund(TargetUser target, OrderRefundExtendInfo extendInfo) {
        var holder = new RefundAccumulateHolder();
        var chain = OrderRefundCalculatorChain.createCalculator(this.context);
        chain.preCalculate(target, extendInfo, holder);
        var infoEntity = this.processRefundInfo(target, extendInfo, holder);
        var refundId = infoEntity.getRefundOrderId();
        chain.postCalculate(refundId, target, extendInfo, holder);
        this.additionalService.saveAdditionalInfo(refundId, extendInfo.extendInfo());
        infoEntity.addDelayTasks(holder.getDelayTasks());
        return refundId;
    }

    private OrderRefundInfoEntity processRefundInfo(TargetUser target, OrderRefundExtendInfo extendInfo, RefundAccumulateHolder holder) {
        var id = UUID.randomUUID().toString();
        var infoEntity = new OrderRefundInfoEntity().setRefundOrderId(id).setRefundStatus(OrderRefundStatusEnum.FINISH.value())
                .setTargetId(target.targetId()).setCampus(target.campus()).setTarget(target).setTargetToast(Jackson.writeString(new TargetUserDesAdapter(target)));
        this.refundInfoRepository.saveAndFlush(infoEntity);
        var additional = extendInfo.additionalFee();
        var wallet = extendInfo.walletRefund();
        var feeEntity = new OrderRefundFeeEntity().setRefundOrderId(id).setTotalOverPrice(holder.getTotalOverPrice()).setDueRefundPrice(holder.getDueRefundPrice())
                .setFinalRefundPrice(holder.getFinalRefundPrice()).setConfirmFee(additional.confirmFee()).setCardFee(additional.cardFee()).setDataFee(additional.dataFee())
                .setHandlingFee(additional.handlingFee()).setOfflineFee(additional.offlineFee()).setManageFee(additional.manageFee()).setUseWallet(wallet.useWallet());
        this.refundFeeRepository.saveAndFlush(feeEntity);
        return infoEntity;
    }

    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void modifyExistRefundOrder(String refundOrderId, RefundExtendInfo extendInfo) {
        Consumer<OrderRefundInfoEntity> consumer = entity -> OrderRefundStatusEnum.of(entity.getRefundStatus()).getHandler(this.context).processOrderRefundModify(refundOrderId, extendInfo, entity);
        this.refundInfoRepository.findByRefundOrderId(refundOrderId).ifPresent(consumer);
    }

    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void cancelRefundOrder(AtomicReference<String> refundOrderIdRef) {
        Consumer<OrderRefundInfoEntity> consumer = entity -> OrderRefundStatusEnum.of(entity.getRefundStatus()).getHandler(this.context).processOrderRefundCancel(entity.getRefundOrderId(), entity);
        this.refundInfoRepository.findByRefundOrderId(refundOrderIdRef.get()).ifPresent(consumer);
    }

    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void deleteRefundOrder(AtomicReference<String> refundOrderIdRef) {
        Consumer<OrderRefundInfoEntity> consumer = entity -> OrderRefundStatusEnum.of(entity.getRefundStatus()).getHandler(this.context).processOrderRefundDelete(entity.getRefundOrderId(), entity);
        this.refundInfoRepository.findByRefundOrderId(refundOrderIdRef.get()).ifPresent(consumer);
    }

}
