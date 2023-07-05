package com.star.enterprise.order.refund.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

import static com.star.enterprise.order.refund.data.jpa.entity.OrderRefundItemFeeEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_orife_rid", columnList = "refundOrderId"),
        @Index(name = "idx_orife_roid", columnList = "refundOrderItemId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderRefundItemFeeEntity {

    protected static final String TABLE_NAME = "order_refund_item_fee";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    /**
     * 退费订单id
     */
    private String refundOrderId;

    /**
     * 退费项id
     */
    private String refundOrderItemId;

    /**
     * 收费订单id
     */
    private String orderId;

    /**
     * 订单物品id
     */
    private String orderItemId;

    /**
     * 退费时课程标准单价
     */
    private BigDecimal currentStandardPrice;

    /**
     * 当前余额
     */
    private BigDecimal currentBalance;

    /**
     * 退正课数量
     */
    private BigDecimal refundNumber;

    /**
     * 退赠送数量
     */
    private BigDecimal refundApportion;

    /**
     * 退款正课金额
     */
    private BigDecimal refundNumberPrice;

    /**
     * 退款赠课金额
     */
    private BigDecimal refundApportionPrice;

    /**
     * 退费溢价
     */
    private BigDecimal refundOverPrice;

}
