package com.star.enterprise.order.core.calculator.processor;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculateProcessor;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.data.jpa.OrderWalletCacheRepository;
import com.star.enterprise.order.core.data.jpa.entity.OrderWalletCacheEntity;
import com.star.enterprise.order.remote.wallet.RemoteWalletBalanceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.star.enterprise.order.base.exception.RestCode.CORE_OPERATE_ERROR;

/**
 * 使用零钱包
 * @author xiaowenrou
 * @date 2022/9/26
 */
@Slf4j
@Component
@AllArgsConstructor
public class WalletProcessor implements CalculateProcessor {

    @Override
    public int getOrder() {
        return 2000;
    }

    private final RemoteWalletBalanceService balanceService;

    private final OrderWalletCacheRepository walletCacheRepository;

    @Override
    public void preCalculate(OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        var wallet = orderDetail.discount().walletDiscount();
        var balance = this.balanceService.fetchBalance(target.targetId(), target.campus());
        wallet.context().setCurrentBalance(balance);
        if (BigDecimal.ZERO.compareTo(wallet.useWallet()) < 0) {
            if (balance.compareTo(wallet.useWallet()) < 0) {
                throw new BusinessWarnException(CORE_OPERATE_ERROR, "error.order.balance");
            }
            wallet.context().setCanUse(true);
            holder.subtractOrderDueCollectPrice(wallet.useWallet());
        }
        chain.preCalculate(orderDetail, chain, holder, target);
    }

    @Override
    public void postCalculate(String orderId, OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        var wallet = orderDetail.discount().walletDiscount();
        var te = this.walletCacheRepository.findByOrderId(orderId).orElseGet(() -> new OrderWalletCacheEntity().setOrderId(orderId).setTargetId(target.targetId())
                .setTargetCampusId(target.campus())).setCurrentUseWallet(wallet.useWallet()).setWalletBalance(wallet.context().getCurrentBalance()).setCommitRecordId(wallet.context().getCommitId());
        this.walletCacheRepository.saveAndFlush(te);
        chain.postCalculate(orderId, orderDetail, chain, holder, target);
    }

}
