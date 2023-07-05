package com.star.enterprise.order.core.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

import static com.star.enterprise.order.core.data.jpa.entity.OrderWalletCacheEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * 目标用户的快照
 * @author xiaowenrou
 * @date 2022/10/26
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_owce_order_target_campus_id", columnList = "orderId, targetId, targetCampusId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderWalletCacheEntity {

    protected static final String TABLE_NAME = "order_wallet_cache";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME+"_seq", allocationSize = 1)
    private Long id;

    private String orderId;

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

    /*
     * 事务id
     */
    private String commitRecordId;

}
