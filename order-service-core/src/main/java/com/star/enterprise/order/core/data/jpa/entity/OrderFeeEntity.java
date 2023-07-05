package com.star.enterprise.order.core.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import java.math.BigDecimal;

import static com.star.enterprise.order.core.data.jpa.entity.OrderFeeEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * 订单总费用表
 * @author xiaowenrou
 * @date 2022/9/22
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_ofe_order_id", columnList = "orderId")
})
public class OrderFeeEntity {

    protected static final String TABLE_NAME = "order_fee";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME+"_seq", allocationSize = 1)
    private Long id;

    private String orderId;

    private String subOrderId;

    /**
     * 原总价
     */
    private BigDecimal orderOriginPrice;

    /**
     * 折后总价
     */
    private BigDecimal orderAfterDiscountPrice;

    /**
     * 使用直减金额
     */
    private BigDecimal useDirect;

    /**
     * 使用优惠券
     */
    private BigDecimal useCoupons;

    /**
     * 使用零钱包的金额
     */
    private BigDecimal useWallet;

    /**
     * 应实金额
     */
    private BigDecimal orderDueCollectPrice;


}
