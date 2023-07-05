package com.star.enterprise.order.charge.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import static com.star.enterprise.order.charge.data.jpa.entity.ChargePropertiesEntity.TABLE_NAME;
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
        @Index(name = "idx_cpe_charge_id", columnList = "chargeItemId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class ChargePropertiesEntity {

    protected static final String TABLE_NAME = "charge_properties";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME+"_seq", allocationSize = 10)
    private Long id;

    /**
     * 收费项id
     */
    private String chargeItemId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 属性名
     */
    private String property;

    /**
     * 属性名
     */
    private String propertyDesc;

    /**
     * 标准属性值
     */
    private String standardValue;

    /**
     * 最小属性值
     */
    private String minValue;

    /**
     * 最大属性值
     */
    private String maxValue;

    /**
     * 属性附加信息
     */
    private String attribute;

    /**
     * 透传字段
     */
    private String penetrate;

}
