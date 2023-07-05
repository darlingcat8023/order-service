package com.star.enterprise.order.refund.model.trans;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundItemInfoEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Data
@Accessors(chain = true)
public class OrderRefundItemSummaryTransObject {

    /**
     * 收费收据号
     */
    private String orderPaidReceiptNo;

    /**
     * 退费订单id
     */
    private String refundOrderId;

    /**
     * 退费项id
     */
    private String refundOrderItemId;

    /**
     * 收费订单id
     */
    private String orderId;

    /**
     * 订单物品id
     */
    private String orderItemId;

    /**
     * 业务id
     */
    private String businessId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务名
     */
    private String businessName;

    /**
     * 规格id
     */
    private String specId;

    /**
     * 规格名
     */
    private String specName;

    /**
     * 购买时间
     */
    private LocalDateTime purchaseDate;

    /**
     * 附加属性
     */
    @JsonRawValue
    private String extendInfo;

    /**
     * 透传字段
     */
    private String toast;

    /**
     * web前端展示逻辑
     */
    private String webViewToast;

    /**
     * 购买数量
     */
    private BigDecimal number;

    /**
     * 剩余购买数量
     */
    private BigDecimal numberLeft;

    /**
     * 赠送数量
     */
    private BigDecimal apportion;

    /**
     * 剩余赠送数量
     */
    private BigDecimal apportionLeft;

    /**
     * 原始单价
     */
    private BigDecimal originSinglePrice;

    /**
     * 原始总价
     */
    private BigDecimal originTotalPrice;

    /**
     * 应收单价
     */
    private BigDecimal dueCollectPrice;

    /**
     * 应收总价
     */
    private BigDecimal dueCollectSinglePrice;

    private OrderRefundItemFeeSummaryTransObject itemFee;

    public OrderRefundItemSummaryTransObject(OrderRefundItemInfoEntity entity, OrderRefundItemFeeSummaryTransObject itemFee) {
        BeanUtils.copyProperties(entity, this);
        this.itemFee = itemFee;
    }

}
