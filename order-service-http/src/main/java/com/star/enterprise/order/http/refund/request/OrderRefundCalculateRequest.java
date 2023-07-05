package com.star.enterprise.order.http.refund.request;

import com.star.enterprise.order.core.model.impl.DefaultWalletDiscountDetail;
import com.star.enterprise.order.http.order.request.TargetRecord;
import com.star.enterprise.order.refund.model.OrderRefundInfo;
import com.star.enterprise.order.refund.model.OrderRefundItemInfo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public record OrderRefundCalculateRequest(
        @Valid
        @NotNull(message = "refund info not valid")
        @Size(message = "illegal refund item", min = 1)
        List<OrderRefundItemRecord> records,

        @Valid
        @NotNull(message = "additional fee not valid")
        OrderRefundAdditionalFeeRecord additionalFee,

        @Valid
        @NotNull(message = "target info not valid")
        TargetRecord target,

        BigDecimal useWallet,

        DefaultWalletDiscountDetail walletRefund


) implements OrderRefundInfo {

        public OrderRefundCalculateRequest(List<OrderRefundItemRecord> records, OrderRefundAdditionalFeeRecord additionalFee, TargetRecord target, BigDecimal useWallet,
                                           DefaultWalletDiscountDetail walletRefund) {
                this.records = records;
                this.additionalFee = additionalFee;
                this.target = target;
                this.useWallet = Objects.requireNonNullElseGet(useWallet, () -> new BigDecimal(0));
                this.walletRefund = new DefaultWalletDiscountDetail(this.useWallet);
        }

        @Override
        public List<OrderRefundItemInfo> items() {
                return new ArrayList<>(this.records);
        }

}
