package com.star.enterprise.order.transfer.model;

import com.star.enterprise.order.core.model.OrderBusinessInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public interface OrderTransferItemInfo extends OrderBusinessInfo {

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
    BigDecimal transferNumber();

    /**
     * 退款赠送数量
     * @return
     */
    BigDecimal transferApportion();

    /**
     * 操作费用项
     * @return
     */
    TransferFeeOperator operator();

    /**
     * 上下文
     * @return
     */
    TransferOrderItemContext context();

}
