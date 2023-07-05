package com.star.enterprise.order.refund.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import static com.star.enterprise.order.refund.data.jpa.entity.OrderRefundAdditionInfoEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_oraie_rid", columnList = "refundOrderId"),
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderRefundAdditionInfoEntity {

    protected static final String TABLE_NAME = "order_refund_addition_info";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    /**
     * 订单号
     */
    private String refundOrderId;

    /**
     * 退款方式
     */
    private String refundMethod;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 银行id
     */
    private String bankId;

    /**
     * 银行名
     */
    private String bankName;

    /**
     * 银行账号
     */
    private String cardNumber;

    /**
     * 备注
     */
    @Column(columnDefinition = "text")
    private String remark;

    /**
     * 退款原因
     */
    @Column(columnDefinition = "text")
    private String refundReason;

    /**
     * 附件列表
     */
    @Column(columnDefinition = "text")
    private String file;

}
