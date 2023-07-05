package com.star.enterprise.order.refund.model.trans;

import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundFeeEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Data
@Accessors(chain = true)
public class OrderRefundFeeSummaryTransObject {


    private String refundOrderId;

    private BigDecimal totalOverPrice;

    private BigDecimal useWallet;

    /**
     * 应退费金额
     */
    private BigDecimal dueRefundPrice;

    /**
     * 确认收入
     */
    private BigDecimal confirmFee;

    /**
     * 管理费
     */
    private BigDecimal manageFee;

    /**
     * 手续费
     */
    private BigDecimal handlingFee;

    /**
     * 刷卡费
     */
    private BigDecimal cardFee;

    /**
     * 线下费用
     */
    private BigDecimal offlineFee;

    /**
     * 资料费
     */
    private BigDecimal dataFee;

    /**
     * 实际退费金额
     */
    private BigDecimal finalRefundPrice;

    public OrderRefundFeeSummaryTransObject(OrderRefundFeeEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }

}
