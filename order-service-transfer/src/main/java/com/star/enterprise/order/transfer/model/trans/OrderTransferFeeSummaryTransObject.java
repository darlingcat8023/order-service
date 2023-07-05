package com.star.enterprise.order.transfer.model.trans;

import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferFeeEntity;
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
public class OrderTransferFeeSummaryTransObject {


    private String transferOrderId;


    private BigDecimal totalOverPrice;

    /**
     * 应退费金额
     */
    private BigDecimal dueTransferPrice;

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
    private BigDecimal finalTransferPrice;

    public OrderTransferFeeSummaryTransObject(OrderTransferFeeEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }

}
