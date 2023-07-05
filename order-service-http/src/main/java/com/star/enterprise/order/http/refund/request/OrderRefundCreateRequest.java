package com.star.enterprise.order.http.refund.request;

import com.star.enterprise.order.core.model.impl.DefaultWalletDiscountDetail;
import com.star.enterprise.order.http.order.request.TargetRecord;
import com.star.enterprise.order.refund.model.OrderRefundExtendInfo;
import com.star.enterprise.order.refund.model.OrderRefundItemInfo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public record OrderRefundCreateRequest(

        @Valid
        @NotNull(message = "target info not valid")
        TargetRecord target,

        @Valid
        List<OrderRefundItemRecord> records,

        @Valid
        @NotNull(message = "additional fee not valid")
        OrderRefundAdditionalFeeRecord additionalFee,

        BigDecimal useWallet,

        @Valid
        @NotNull(message = "extend info not valid")
        OrderRefundExtendRecord extendInfo,

        DefaultWalletDiscountDetail walletRefund

) implements OrderRefundExtendInfo {

    public OrderRefundCreateRequest(TargetRecord target, List<OrderRefundItemRecord> records, OrderRefundAdditionalFeeRecord additionalFee, BigDecimal useWallet, OrderRefundExtendRecord extendInfo,
                                    DefaultWalletDiscountDetail walletRefund) {
        this.target = target;
        this.records = records;
        this.additionalFee = additionalFee;
        this.useWallet = Objects.requireNonNullElseGet(useWallet, () -> new BigDecimal(0));
        this.extendInfo = extendInfo;
        this.walletRefund = new DefaultWalletDiscountDetail(this.useWallet);
    }

    @Override
    public List<OrderRefundItemInfo> items() {
        return new ArrayList<>(this.records);
    }

}
