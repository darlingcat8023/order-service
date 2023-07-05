package com.star.enterprise.order.refund.model;

import com.star.enterprise.order.core.model.OrderBusinessInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public interface OrderRefundItemInfo extends OrderBusinessInfo {

    /**
     * 收据号
     * @return
     */
    String receiptNo();

    /**
     * 发票号
     * @return
     */
    String invoiceNo();

    /**
     * 购买日期
     * @return
     */
    LocalDateTime purchasedDate();

    /**
     * 订单号
     * @return
     */
    String orderId();

    /**
     * 订单物品编号
     * @return
     */
    String orderItemId();

    /**
     * 退款购买数量
     * @return
     */
    BigDecimal refundNumber();

    /**
     * 退款赠送数量
     * @return
     */
    BigDecimal refundApportion();


    /**
     * 操作费用项
     * @return
     */
    RefundFeeOperator operator();

    /**
     * 上下文
     * @return
     */
    RefundOrderItemContext context();

}
