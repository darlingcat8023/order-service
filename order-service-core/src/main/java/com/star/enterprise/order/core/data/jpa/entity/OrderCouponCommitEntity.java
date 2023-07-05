package com.star.enterprise.order.core.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author xiaowenrou
 * @date 2023/3/24
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = OrderCouponCommitEntity.TABLE_NAME, indexes = {
        @Index(name = "idx_occe_oid", columnList = "orderId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderCouponCommitEntity {

    protected static final String TABLE_NAME = "order_coupon_commit";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 事务id
     */
    private String commitRecordId;

}
