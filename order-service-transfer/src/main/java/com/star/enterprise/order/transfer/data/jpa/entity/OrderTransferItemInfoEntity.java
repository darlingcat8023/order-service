package com.star.enterprise.order.transfer.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.GenerationType.SEQUENCE;


/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = OrderTransferItemInfoEntity.TABLE_NAME, indexes = {
        @Index(name = "idx_otiie_rid_roid", columnList = "transferOrderId, transferOrderItemId"),
        @Index(name = "idx_otiie_oid_oiid", columnList = "orderId, orderItemId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderTransferItemInfoEntity {

    protected static final String TABLE_NAME = "order_transfer_item_info";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    /**
     * 收费收据号
     */
    private String orderPaidReceiptNo;

    /**
     * 结转订单id
     */
    private String transferOrderId;

    /**
     * 退费项id
     */
    private String transferOrderItemId;

    /**
     * 收费订单id
     */
    private String orderId;

    /**
     * 订单物品id
     */
    private String orderItemId;

    /**
     * 业务id
     */
    private String businessId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务名
     */
    private String businessName;

    /**
     * 规格id
     */
    private String specId;

    /**
     * 规格名
     */
    private String specName;

    /**
     * 购买时间
     */
    private LocalDateTime purchaseDate;

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

    /**
     * 购买数量
     */
    private BigDecimal number;

    /**
     * 剩余购买数量
     */
    private BigDecimal numberLeft;

    /**
     * 赠送数量
     */
    private BigDecimal apportion;

    /**
     * 剩余赠送数量
     */
    private BigDecimal apportionLeft;

    /**
     * 原始单价
     */
    private BigDecimal originSinglePrice;

    /**
     * 原始总价
     */
    private BigDecimal originTotalPrice;

    /**
     * 应收单价
     */
    private BigDecimal dueCollectPrice;

    /**
     * 应收总价
     */
    private BigDecimal dueCollectSinglePrice;


    @PrePersist
    public void prePersist() {
        this.transferOrderItemId = UUID.randomUUID().toString();
    }

}
