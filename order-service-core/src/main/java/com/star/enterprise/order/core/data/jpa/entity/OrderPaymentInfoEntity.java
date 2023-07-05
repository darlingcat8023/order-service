package com.star.enterprise.order.core.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

import static com.star.enterprise.order.core.data.jpa.entity.OrderPaymentInfoEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author xiaowenrou
 * @date 2022/10/25
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_opie_order_id", columnList = "orderId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderPaymentInfoEntity {

    protected static final String TABLE_NAME = "order_payment_info";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME+"_seq", allocationSize = 1)
    private Long id;

    private String orderId;

    private String paymentAccount;

    private String paymentMethod;

    private BigDecimal paymentAmount;

}
