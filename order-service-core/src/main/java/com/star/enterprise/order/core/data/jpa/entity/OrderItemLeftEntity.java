package com.star.enterprise.order.core.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.math.BigDecimal;

import static com.star.enterprise.order.core.data.jpa.entity.OrderItemLeftEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author xiaowenrou
 * @date 2023/4/3
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_oile_oiid", columnList = "orderItemId"),
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderItemLeftEntity {

    protected static final String TABLE_NAME = "order_item_left";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    private String orderItemId;

    @Version
    private Integer version;

    private String businessId;

    private String businessType;

    private BigDecimal numberLeft;

    private BigDecimal apportionLeft;

}
