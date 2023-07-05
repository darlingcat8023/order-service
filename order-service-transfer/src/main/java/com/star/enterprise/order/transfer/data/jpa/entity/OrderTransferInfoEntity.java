package com.star.enterprise.order.transfer.data.jpa.entity;

import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.adapter.TargetUserDesAdapter;
import com.star.enterprise.order.core.calculator.CalculatorDelayTask;
import com.star.enterprise.order.transfer.constants.OrderTransferStatusEnum;
import com.star.enterprise.order.transfer.event.OrderTransferInfoAsyncEvent;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferInfoEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;


/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_otie_rid_sta", columnList = "transferOrderId, transferStatus"),
        @Index(name = "idx_otie_tid", columnList = "targetId"),
        @Index(name = "idx_otie_aid", columnList = "approvalId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderTransferInfoEntity {

    protected static final String TABLE_NAME = "order_transfer_info";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    @Version
    private Integer version;

    /**
     * 退费订单id
     */
    private String transferOrderId;

    /**
     * 审批id
     */
    private String approvalId;

    /**
     * 创建日期
     */
    @CreatedDate
    private LocalDateTime createdDate;

    /**
     * 更新日期
     */
    @LastModifiedDate
    private LocalDateTime lastModifyDate;

    /**
     * 操作人
     */
    @CreatedBy
    private String operator;

    /**
     * 用户id
     */
    private String targetId;

    /**
     * 校区id
     */
    private String campus;

    /**
     * 退费状态
     */
    private String transferStatus;

    /**
     * 页面透传
     */
    @Column(columnDefinition = "text")
    private String webViewToast;

    /**
     * 用户透传
     */
    @Column(columnDefinition = "text")
    private String targetToast;

    @Transient
    private TargetUser target;

    @Transient
    private List<CalculatorDelayTask> delayTasks = new ArrayList<>();

    public void addDelayTasks(Collection<CalculatorDelayTask> collection) {
        this.delayTasks.addAll(collection);
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
    public List<OrderTransferInfoAsyncEvent> domainEvents() {
        return List.of(new OrderTransferInfoAsyncEvent(this.transferOrderId, OrderTransferStatusEnum.of(this.transferStatus), this.version, this.target, this.delayTasks));
    }

}
