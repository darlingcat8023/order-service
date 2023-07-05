package com.star.enterprise.order.receipt.data.jpa.entity;

import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.adapter.TargetUserDesAdapter;
import com.star.enterprise.order.receipt.constants.ReceiptStatusEnum;
import com.star.enterprise.order.receipt.constants.ReceiptTypeEnum;
import com.star.enterprise.order.receipt.event.ReceiptAsyncEvent;
import com.star.enterprise.order.receipt.model.ReceiptContext;
import com.star.enterprise.order.receipt.service.Receipt;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author xiaowenrou
 * @date 2022/11/28
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = ReceiptOrderPaidEntity.TABLE_NAME, indexes = {
        @Index(name = "idx_rop_order_id", columnList = "orderId"),
        @Index(name = "idx_rop_rec_no", columnList = "receiptNo")
})
public class ReceiptOrderPaidEntity implements Receipt {

    protected static final String TABLE_NAME = "receipt_order_paid";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    private String receiptNo;

    private String orderId;

    private String targetId;

    private String receiptStatus;

    private Integer print;

    @Column(columnDefinition = "text")
    private String targetToast;

    @Column(columnDefinition = "text")
    private String toast;

    @Transient
    private TargetUser target;

    @Transient
    private ReceiptContext context;

    @Override
    public String receiptNo() {
        return this.receiptNo;
    }

    @Override
    public String orderId() {
        return this.orderId;
    }

    @Override
    public TargetUser target() {
        return this.target;
    }

    @Override
    public ReceiptStatusEnum status() {
        return ReceiptStatusEnum.of(this.receiptStatus);
    }

    @Override
    public ReceiptTypeEnum type() {
        return ReceiptTypeEnum.ORDER_PAID;
    }

    @Override
    public Integer print() {
        return this.print;
    }

    @PostLoad
    public void postLoad() {
        this.target = Jackson.read(this.getTargetToast(), TargetUserDesAdapter.class);
    }

    /**
     * 发布保存事件
     * @return
     */
    @DomainEvents
    public List<ReceiptAsyncEvent> domainEvents() {
        return List.of(new ReceiptAsyncEvent(this, this.context));
    }

}
