package com.star.enterprise.order.refund.model;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
@Getter
public class RefundFeeOperator {

    /**
     * 当前余额
     */
    private BigDecimal currentBalance = new BigDecimal(0);

    /**
     * 退费金额
     */
    private BigDecimal refundNumberPrice = new BigDecimal(0);

    /**
     * 退费赠送金额
     */
    private BigDecimal refundApportionPrice = new BigDecimal(0);

    /**
     * 退费溢价
     */
    private BigDecimal refundOverPrice = new BigDecimal(0);

    public void setCurrentBalance(BigDecimal balance) {
        this.currentBalance = balance;
    }

    public void addRefundNumberPrice(BigDecimal price) {
        this.refundNumberPrice = this.refundNumberPrice.add(price);
    }

    public void addRefundApportionPrice(BigDecimal price) {
        this.refundApportionPrice = this.refundApportionPrice.add(price);
    }

    public void setRefundOverPrice(BigDecimal price) {
        this.refundOverPrice = price;
    }

    public BigDecimal getRefundTotalPrice() {
        return this.refundNumberPrice.add(this.refundApportionPrice);
    }

}
