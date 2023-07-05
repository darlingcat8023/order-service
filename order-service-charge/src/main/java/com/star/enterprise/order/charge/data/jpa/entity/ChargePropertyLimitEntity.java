package com.star.enterprise.order.charge.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.star.enterprise.order.charge.data.jpa.entity.ChargePropertyLimitEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Accessors(chain = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_order_charge_id", columnList = "orderId, chargeItemId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class ChargePropertyLimitEntity {

    protected static final String TABLE_NAME = "charge_property_limit";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME+"_seq", allocationSize = 1)
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String orderId;

    private String orderItemId;

    private String targetId;

    private String campusId;

    private String chargeItemId;

}
