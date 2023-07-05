package com.star.enterprise.order.receipt.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * 时序表
 * @author xiaowenrou
 * @date 2022/12/1
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = ReceiptSequenceEntity.TABLE_NAME, indexes = {
        @Index(name = "idx_rs_recp_no", columnList = "receiptNo")
})
public class ReceiptSequenceEntity {

    protected static final String TABLE_NAME = "receipt_sequence";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    private LocalDateTime createdAt;

    private String receiptNo;

    private String targetId;

    private String campus;

    private String orderId;

}
