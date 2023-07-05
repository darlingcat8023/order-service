package com.star.enterprise.order.transfer.model;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
@Getter
public class TransferFeeOperator {

    /**
     * 当前余额
     */
    private BigDecimal currentBalance = new BigDecimal(0);

    /**
     * 退费金额
     */
    private BigDecimal transferNumberPrice = new BigDecimal(0);

    /**
     * 退费赠送金额
     */
    private BigDecimal transferApportionPrice = new BigDecimal(0);

    public void setCurrentBalance(BigDecimal balance) {
        this.currentBalance = balance;
    }

    public void addTransferNumberPrice(BigDecimal price) {
        this.transferNumberPrice = this.transferNumberPrice.add(price);
    }

    public void addTransferApportionPrice(BigDecimal price) {
        this.transferApportionPrice = this.transferApportionPrice.add(price);
    }

    public BigDecimal getTransferTotalPrice() {
        return this.transferNumberPrice.add(this.transferApportionPrice);
    }

}
