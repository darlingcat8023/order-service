package com.star.enterprise.order.receipt.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author xiaowenrou
 * @date 2022/12/6
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = ReceiptLogEntity.TABLE_NAME, indexes = {
        @Index(name = "idx_rl_rec_no", columnList = "receiptNo")
})
public class ReceiptLogEntity {

    protected static final String TABLE_NAME = "receipt_log";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    private LocalDateTime createdAt;

    private String orderId;

    private String receiptNo;

    private String operatorId;

    private String operator;

    private String action;

}
