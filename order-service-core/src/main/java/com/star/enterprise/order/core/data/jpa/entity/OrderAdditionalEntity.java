package com.star.enterprise.order.core.data.jpa.entity;


import com.fasterxml.jackson.core.type.TypeReference;
import com.star.enterprise.order.base.utils.Jackson;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

import static com.star.enterprise.order.core.data.jpa.entity.OrderAdditionalEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * 订单发票表
 * @author xiaowenrou
 * @date 2022/9/22
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_oae_order_id", columnList = "orderId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderAdditionalEntity {

    protected static final String TABLE_NAME = "order_additional_info";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    private String orderId;

    private String invoiceNo;

    private BigDecimal invoiceAmount;

    @Column(columnDefinition = "text")
    private String certs;

    private String innerRemark;

    private String outerRemark;

    @Transient
    private List<String> certsList;

    @PostLoad
    public void postLoad() {
        this.certsList = StringUtils.hasText(this.certs) ? Jackson.read(this.certs, new TypeReference<>() {}) : List.of();
    }

}
