package com.star.enterprise.order.core.data.jpa.entity;


import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import static com.star.enterprise.order.core.data.jpa.entity.OrderPerformanceEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * 订单业绩归属人表
 * @author xiaowenrou
 * @date 2022/9/22
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_ope_order_id", columnList = "orderId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderPerformanceEntity {

    protected static final String TABLE_NAME = "order_performance";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    private String orderId;

    private String userId;

    private String userName;

    private String userRole;

}
