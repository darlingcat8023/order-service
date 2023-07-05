package com.star.enterprise.order.refund.calculator;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.refund.data.jpa.OrderRefundWalletCacheRepository;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundWalletCacheEntity;
import com.star.enterprise.order.refund.model.OrderRefundInfo;
import com.star.enterprise.order.refund.model.trans.OrderRefundSummaryTransObject;
import com.star.enterprise.order.remote.wallet.RemoteWalletBalanceService;
import com.star.enterprise.wallet.api.request.WalletBalanceChangeRecord;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

import static com.star.enterprise.order.base.exception.RestCode.REFUND_OPERATE_ERROR;

/**
 * @author xiaowenrou
 * @date 2023/3/16
 */
@Component
@AllArgsConstructor
public class RefundWalletProcessor implements OrderRefundCalculator {

    private final OrderRefundWalletCacheRepository walletCacheRepository;

    private final RemoteWalletBalanceService walletBalanceService;

    @Override
    public void preCalculate(TargetUser target, OrderRefundInfo refundInfo, RefundAccumulateHolder holder) {
        var wallet = refundInfo.walletRefund();
        var balance = this.walletBalanceService.fetchBalance(target.targetId(), target.campus());
        wallet.context().setCurrentBalance(balance);
        if (BigDecimal.ZERO.compareTo(wallet.useWallet()) < 0) {
            if (balance.compareTo(wallet.useWallet()) < 0) {
                throw new BusinessWarnException(REFUND_OPERATE_ERROR, "error.refund.balance");
            }
            wallet.context().setCanUse(true);
            holder.addDueRefundPrice(wallet.useWallet());
        }
    }

    @Override
    public void postCalculate(String refundOrderId, TargetUser target, OrderRefundInfo refundInfo, RefundAccumulateHolder holder) {
        var wallet = refundInfo.walletRefund();
        var te = new OrderRefundWalletCacheEntity().setRefundOrderId(refundOrderId).setTargetId(target.targetId())
                .setTargetCampusId(target.campus()).setCurrentUseWallet(wallet.useWallet()).setWalletBalance(wallet.context().getCurrentBalance());
        this.walletCacheRepository.saveAndFlush(te);
        if (wallet.context().isCanUse()) {
            var record = this.walletBalanceService.freezeBalance(target.targetId(), target.campus(), new WalletBalanceChangeRecord(refundOrderId, "refund", wallet.useWallet(), ""));
            holder.addDelayTask(order -> this.walletBalanceService.commitRecord(record));
        }
    }

    @Override
    public void postConsumerElastic(Map<String, OrderRefundSummaryTransObject> objects) {
        OrderRefundCalculator.super.postConsumerElastic(objects);
    }

}
