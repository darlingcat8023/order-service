package com.star.enterprise.order.transfer.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

import static com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferFeeEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;


/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_otfe_rid", columnList = "transferOrderId"),
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderTransferFeeEntity {

    protected static final String TABLE_NAME = "order_transfer_fee";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    private String transferOrderId;

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


}
