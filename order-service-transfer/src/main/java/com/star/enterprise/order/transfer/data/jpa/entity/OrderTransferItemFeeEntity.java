package com.star.enterprise.order.transfer.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

import static com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferItemFeeEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_otife_rid", columnList = "transferOrderId"),
        @Index(name = "idx_otife_roid", columnList = "transferOrderItemId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderTransferItemFeeEntity {

    protected static final String TABLE_NAME = "order_transfer_item_fee";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    /**
     * 退费订单id
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
     * 退费时课程标准单价
     */
    private BigDecimal currentStandardPrice;

    private BigDecimal currentBalance;

    /**
     * 结转正课数量
     */
    private BigDecimal transferNumber;

    /**
     * 结转赠送数量
     */
    private BigDecimal transferApportion;

    /**
     * 结转正课金额
     */
    private BigDecimal transferNumberPrice;

    /**
     * 结转赠课金额
     */
    private BigDecimal transferApportionPrice;

}
