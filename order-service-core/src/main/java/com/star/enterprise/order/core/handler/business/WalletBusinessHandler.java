package com.star.enterprise.order.core.handler.business;

import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.charge.matcher.business.WalletChargeBusiness;
import com.star.enterprise.order.charge.matcher.business.WalletChargeBusinessMatcher;
import com.star.enterprise.order.charge.model.Fee;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.OrderItemFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.remote.wallet.RemoteWalletBalanceService;
import com.star.enterprise.wallet.api.request.WalletBalanceChangeRecord;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/4/19
 */
@Component
@AllArgsConstructor
public class WalletBusinessHandler extends BusinessTypeHandler {

    private final RemoteWalletBalanceService walletBalanceService;

    private final WalletChargeBusinessMatcher matcher;

    @Override
    public void processItemBusinessInfo(TargetUser target, OrderItemFeeDetail item, DelegatingAccumulateHolder holder) {
        item.context().setProductName("钱包充值");
        var opt = item.operator();
        opt.setOriginalSinglePrice(new BigDecimal(item.number()));
        opt.setOriginalTotalPrice(new BigDecimal(item.number()));
        super.processItemBusinessInfo(target, item, holder);
    }

    @Override
    public void processItemAfterSaved(String orderId, TargetUser target, OrderItemFeeDetail item, DelegatingAccumulateHolder holder) {
        var record = new WalletBalanceChangeRecord(orderId, "purchase", new BigDecimal(item.number()), Jackson.writeString(item));
        holder.addDelayTask(businessId -> this.walletBalanceService.addBalance(item.businessId(), record));
        super.processItemAfterSaved(orderId, target, item, holder);
    }

    @Override
    public List<MatchResult> calculateChargeMatchResult(TargetUser target, OrderItemFeeDetail item) {
        return this.matcher.matchSingle(target, new WalletChargeAdapter(item));
    }

    @AllArgsConstructor
    public static class WalletChargeAdapter implements OrderItemFeeDetail, WalletChargeBusiness {

        @Delegate
        private final OrderItemFeeDetail delegate;

        @Override
        public String uniqueId() {
            return this.delegate.businessId();
        }

        @Override
        public Fee fee() {
            return this.delegate.operator();
        }

        @Override
        public String walletId() {
            return this.delegate.businessId();
        }
    }


}
