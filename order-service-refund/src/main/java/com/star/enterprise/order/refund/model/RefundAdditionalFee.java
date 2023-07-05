package com.star.enterprise.order.refund.model;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public interface RefundAdditionalFee {

    /**
     * 确认收入
     */
    BigDecimal confirmFee();

    /**
     * 管理费
     */
    BigDecimal manageFee();

    /**
     * 手续费
     */
    BigDecimal handlingFee();

    /**
     * 刷卡费
     */
    BigDecimal cardFee();

    /**
     * 线下费用
     */
    BigDecimal offlineFee();

    /**
     * 资料费
     */
    BigDecimal dataFee();

}
