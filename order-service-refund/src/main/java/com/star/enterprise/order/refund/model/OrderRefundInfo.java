package com.star.enterprise.order.refund.model;

import com.star.enterprise.order.core.calculator.WalletDiscountDetail;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public interface OrderRefundInfo {

    /**
     * 退款项
     * @return
     */
    List<OrderRefundItemInfo> items();

    /**
     * 附加费用
     * @return
     */
    RefundAdditionalFee additionalFee();

    /**
     * 退零钱包
     * @return
     */
    WalletDiscountDetail walletRefund();

}
