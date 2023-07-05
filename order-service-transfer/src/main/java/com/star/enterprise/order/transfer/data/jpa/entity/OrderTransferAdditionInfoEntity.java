package com.star.enterprise.order.transfer.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = OrderTransferAdditionInfoEntity.TABLE_NAME, indexes = {
        @Index(name = "idx_otaie_tsid", columnList = "transferOrderId"),
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderTransferAdditionInfoEntity {

    protected static final String TABLE_NAME = "order_transfer_addition_info";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    /**
     * 订单号
     */
    private String transferOrderId;

    /**
     * 备注
     */
    @Column(columnDefinition = "text")
    private String remark;

    /**
     * 退款原因
     */
    @Column(columnDefinition = "text")
    private String transferReason;

    /**
     * 附件列表
     */
    @Column(columnDefinition = "text")
    private String file;

}
