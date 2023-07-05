package com.star.enterprise.order.refund.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

import static com.star.enterprise.order.refund.data.jpa.entity.OrderRefundWalletCacheEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author xiaowenrou
 * @date 2023/3/16
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_orwce_rfid", columnList = "refundOrderId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderRefundWalletCacheEntity {

    protected static final String TABLE_NAME = "order_refund_wallet_cache";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME+"_seq", allocationSize = 1)
    private Long id;

    private String refundOrderId;

    /**
     * 目标用户id
     */
    private String targetId;

    /**
     * 目标用户校区
     */
    private String targetCampusId;

    /**
     * 当前使用的零钱包
     */
    private BigDecimal currentUseWallet;

    /**
     * 零钱包余额
     */
    private BigDecimal walletBalance;

}
