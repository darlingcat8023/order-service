package com.star.enterprise.order.transfer.service;

import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.adapter.TargetUserDesAdapter;
import com.star.enterprise.order.remote.wallet.RemoteWalletBalanceService;
import com.star.enterprise.order.transfer.calculator.OrderTransferCalculatorChain;
import com.star.enterprise.order.transfer.calculator.TransferAccumulateHolder;
import com.star.enterprise.order.transfer.constants.OrderTransferStatusEnum;
import com.star.enterprise.order.transfer.data.jpa.OrderTransferFeeRepository;
import com.star.enterprise.order.transfer.data.jpa.OrderTransferInfoRepository;
import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferFeeEntity;
import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferInfoEntity;
import com.star.enterprise.order.transfer.model.OrderTransferExtendInfo;
import com.star.enterprise.order.transfer.model.TransferExtendInfo;
import com.star.enterprise.wallet.api.request.WalletBalanceChangeRecord;
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
public class OrderTransferLifecycleService {

    private final ApplicationContext context;

    private final OrderTransferInfoRepository transferInfoRepository;

    private final OrderTransferFeeRepository transferFeeRepository;

    private final OrderTransferAdditionalService additionalService;

    private final RemoteWalletBalanceService walletBalanceService;

    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public String createTransfer(TargetUser target, OrderTransferExtendInfo extendInfo) {
        var holder = new TransferAccumulateHolder();
        var chain = OrderTransferCalculatorChain.createCalculator(this.context);
        chain.preCalculate(target, extendInfo, holder);
        var infoEntity = this.processTransferInfo(target, extendInfo, holder);
        var transferId = infoEntity.getTransferOrderId();
        chain.postCalculate(transferId, target, extendInfo, holder);
        this.additionalService.saveAdditionalInfo(transferId, extendInfo.extendInfo());
        holder.addDelayTask(businessId -> this.walletBalanceService.addBalance(target.targetId(), target.campus(), new WalletBalanceChangeRecord(businessId, "transfer", holder.getFinalTransferPrice(), Jackson.writeString(infoEntity))));
        infoEntity.addDelayTasks(holder.getDelayTasks());
        return transferId;
    }

    private OrderTransferInfoEntity processTransferInfo(TargetUser target, OrderTransferExtendInfo extendInfo, TransferAccumulateHolder holder) {
        var id = UUID.randomUUID().toString();
        var infoEntity = new OrderTransferInfoEntity().setTransferOrderId(id).setTransferStatus(OrderTransferStatusEnum.FINISH.value())
                .setTargetId(target.targetId()).setCampus(target.campus()).setTarget(target).setTargetToast(Jackson.writeString(new TargetUserDesAdapter(target)));
        this.transferInfoRepository.saveAndFlush(infoEntity);
        var additional = extendInfo.additionalFee();
        var feeEntity = new OrderTransferFeeEntity().setTransferOrderId(id).setDueTransferPrice(holder.getDueTransferPrice()).setFinalTransferPrice(holder.getFinalTransferPrice())
                .setConfirmFee(additional.confirmFee()).setCardFee(additional.cardFee()).setDataFee(additional.dataFee()).setHandlingFee(additional.handlingFee())
                .setOfflineFee(additional.offlineFee()).setManageFee(additional.manageFee());
        this.transferFeeRepository.saveAndFlush(feeEntity);
        return infoEntity;
    }

    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void modifyExistTransferOrder(String transferOrderId, TransferExtendInfo extendInfo) {
        Consumer<OrderTransferInfoEntity> consumer = entity -> OrderTransferStatusEnum.of(entity.getTransferStatus()).getHandler(this.context).processOrderTransferModify(transferOrderId, extendInfo, entity);
        this.transferInfoRepository.findByTransferOrderId(transferOrderId).ifPresent(consumer);
    }

    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void cancelTransferOrder(AtomicReference<String> transferOrderIdRef) {
        Consumer<OrderTransferInfoEntity> consumer = entity -> OrderTransferStatusEnum.of(entity.getTransferStatus()).getHandler(this.context).processOrderTransferCancel(entity.getTransferOrderId(), entity);
        this.transferInfoRepository.findByTransferOrderId(transferOrderIdRef.get()).ifPresent(consumer);
    }

    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void deleteTransferOrder(AtomicReference<String> transferOrderIdRef) {
        Consumer<OrderTransferInfoEntity> consumer = entity -> OrderTransferStatusEnum.of(entity.getTransferStatus()).getHandler(this.context).processOrderTransferDelete(entity.getTransferOrderId(), entity);
        this.transferInfoRepository.findByTransferOrderId(transferOrderIdRef.get()).ifPresent(consumer);
    }

}
