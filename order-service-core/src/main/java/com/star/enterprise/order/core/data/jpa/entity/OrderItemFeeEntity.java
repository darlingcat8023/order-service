package com.star.enterprise.order.core.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import java.math.BigDecimal;

import static com.star.enterprise.order.core.data.jpa.entity.OrderItemFeeEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * 订单项费用表
 * @author xiaowenrou
 * @date 2022/9/22
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_oife_order_item_id", columnList = "orderId, orderItemId")
})
public class OrderItemFeeEntity {

    protected static final String TABLE_NAME = "order_item_fee";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME+"_seq", allocationSize = 1)
    private Long id;

    private String orderId;

    private String subOrderId;

    private String orderItemId;

    private String businessType;

    /**
     * 购买数量
     */
    private Integer number;

    /**
     * 原单价
     */
    private BigDecimal originSinglePrice;

    /**
     * 原总价
     */
    private BigDecimal originTotalPrice;

    /**
     * 优惠方案id
     */
    private String discountPlanId;

    /**
     * 折后单价
     */
    private BigDecimal afterDiscountSinglePrice;

    /**
     * 折后总价
     */
    private BigDecimal afterDiscountTotalPrice;

    /**
     * 使用直减金额
     */
    private BigDecimal useDirect;

    /**
     * 使用零钱包的金额
     */
    private BigDecimal useWallet;

    /**
     * 应收单价
     */
    private BigDecimal dueCollectSinglePrice;

    /**
     * 应收金额
     */
    private BigDecimal dueCollectPrice;

    /**
     * 收费类型
     */
    private String chargeCategory;

    /**
     * 收费类型命中的规则id
     */
    private String chargeItemId;

    /**
     * 计算出的收费类型
     */
    @Column(columnDefinition = "text")
    private String matchedChargeCategory;

}
