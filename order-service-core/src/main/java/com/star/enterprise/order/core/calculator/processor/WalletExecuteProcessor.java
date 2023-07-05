package com.star.enterprise.order.core.calculator.processor;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculateProcessor;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import com.star.enterprise.order.remote.wallet.RemoteWalletBalanceService;
import com.star.enterprise.wallet.api.request.WalletBalanceChangeRecord;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2023/3/15
 */
@Component
@AllArgsConstructor
public class WalletExecuteProcessor implements CalculateProcessor {

    @Delegate(types = {PriorityOrdered.class})
    private final WalletProcessor delegate;

    private final RemoteWalletBalanceService balanceService;

    @Override
    public void preCalculate(OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        this.delegate.preCalculate(orderDetail, chain, holder, target);
    }

    @Override
    public void postCalculate(String orderId, OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        var wallet = orderDetail.discount().walletDiscount();
        if (wallet.context().isCanUse()) {
            var record = this.balanceService.freezeBalance(target.targetId(), target.campus(), new WalletBalanceChangeRecord(orderId, "order", wallet.useWallet(), ""));
            holder.addDelayTask(order -> this.balanceService.commitRecord(record));
            wallet.context().setCommitId(record);
        }
        this.delegate.postCalculate(orderId, orderDetail, chain, holder, target);
    }

    @Override
    public void postRollbackProcess(String orderId, TargetUser target, CalculateProcessor chain) {
        this.delegate.postRollbackProcess(orderId, target, chain);
    }

    @Override
    public void preAsyncElastic(String orderId, OrderSearchInfoEntity entity, TargetUser target) {
        this.delegate.preAsyncElastic(orderId, entity, target);
    }

    @Override
    public void postConsumerElastic(Map<String, OrderSummaryTransObject> objects) {
        this.delegate.postConsumerElastic(objects);
    }

}
