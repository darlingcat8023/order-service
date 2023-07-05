package com.star.enterprise.order.core.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

import static com.star.enterprise.order.core.data.jpa.entity.OrderDiscountPlanCacheEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * 优惠方案的交易快照表
 * @author xiaowenrou
 * @date 2022/10/25
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_odpce_oid", columnList = "orderId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderDiscountPlanCacheEntity {

    protected static final String TABLE_NAME = "order_discount_plan_cache";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    private String orderId;

    private String orderItemId;

    private String directDiscountId;

    private String directDiscountName;

    private BigDecimal discountSinglePrice;

    private BigDecimal discountTotalPrice;

    private BigDecimal discountPlanRate;

    private Integer apportion;

}
