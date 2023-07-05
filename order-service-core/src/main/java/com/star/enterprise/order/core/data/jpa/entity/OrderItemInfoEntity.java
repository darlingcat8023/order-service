package com.star.enterprise.order.core.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.star.enterprise.order.core.data.jpa.entity.OrderItemInfoEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * 订单物品信息表
 * @author xiaowenrou
 * @date 2022/9/21
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_oiie_order_item_id", columnList = "orderId, orderItemId"),
        @Index(name = "idx_oiie_order_business_id", columnList = "orderId, businessType, businessId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderItemInfoEntity {

    protected static final String TABLE_NAME = "order_item_info";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME+"_seq", allocationSize = 1)
    private Long id;

    private String orderId;

    private String subOrderId;

    private String orderItemId;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String businessType;

    private String businessId;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 规格id
     */
    private String specId;

    /**
     * 规格名
     */
    private String specName;

    /**
     * 购买数量
     */
    private Integer number;

    /**
     * 原单价
     */
    private BigDecimal originPrice;

    /**
     * 分摊
     */
    private Integer additional;

    /**
     * 附加
     */
    private Integer apportion;

    /**
     * 订单物品状态
     */
    private String itemStatus;

    /**
     * 父id
     */
    private String parentBusinessId;

    /**
     * 回调地址
     */
    private String callbackAddress;

    /**
     * 附加属性
     */
    @Column(columnDefinition = "text")
    private String extendInfo;

    /**
     * 透传字段
     */
    @Column(columnDefinition = "text")
    private String toast;

    /**
     * web前端展示逻辑
     */
    @Column(columnDefinition = "text")
    private String webViewToast;

    @PrePersist
    public void prePersist() {
        this.orderItemId = UUID.randomUUID().toString();
    }

}
