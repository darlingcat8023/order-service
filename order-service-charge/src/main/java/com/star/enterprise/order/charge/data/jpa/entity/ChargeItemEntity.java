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
import java.util.UUID;

import static com.star.enterprise.order.charge.data.jpa.entity.ChargeItemEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;


/**
 * @author xiaowenrou
 * @date 2022/9/14
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_cie_business_tp_id", columnList = "businessType, businessId"),
        @Index(name = "idx_cie_charge_id", columnList = "chargeItemId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class ChargeItemEntity {

    protected static final String TABLE_NAME = "charge_item";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME+"_seq", allocationSize = 1)
    private Long id;

    private String chargeItemId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务id
     */
    private String businessId;

    /**
     * 收费类型
     */
    private String category;

    /**
     * 启用
     */
    private String enable;

    @PrePersist
    public void prePersist() {
        this.chargeItemId = UUID.randomUUID().toString();
    }

}
