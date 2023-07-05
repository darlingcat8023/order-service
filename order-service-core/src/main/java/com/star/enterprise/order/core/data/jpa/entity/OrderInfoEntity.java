package com.star.enterprise.order.core.data.jpa.entity;

import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.adapter.TargetUserDesAdapter;
import com.star.enterprise.order.core.calculator.CalculatorDelayTask;
import com.star.enterprise.order.core.constants.OrderStatusEnum;
import com.star.enterprise.order.core.event.OrderInfoAsyncEvent;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * 订单表
 * @author xiaowenrou
 * @date 2022/9/22
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_oie_id_stu_os", columnList = "orderId, status, orderSource")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderInfoEntity {

    protected static final String TABLE_NAME = "order_info";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME + "_seq", allocationSize = 1)
    private Long id;

    /**
     * 数据版本
     */
    @Version
    private Integer version;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 预留
     */
    private String subOrderId;

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 最后更新时间
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * 删除时间
     */
    private LocalDateTime deletedAt;

    /**
     * 业务时间
     */
    private LocalDateTime businessDate;

    /**
     * 推广渠道
     */
    private String channelId;

    /**
     * 那个业务的订单
     */
    private String orderBusiness;

    /**
     * 订单来源
     */
    private String orderSource;

    /**
     * 制单人
     */
    @CreatedBy
    private String operator;

    /**
     * 最后修改人
     */
    @LastModifiedBy
    private String lastModifiedBy;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 订单完成日期
     */
    private LocalDateTime completedDate;

    /**
     * web前端展示逻辑
     */
    @Column(columnDefinition = "text")
    private String webViewToast;

    /**
     * 用来保存Target对象
     */
    @Column(columnDefinition = "text")
    private String targetToast;

    /**
     * 订单完成回调地址
     */
    private String onFinishCallBack;

    @Transient
    private TargetUser target;

    @Transient
    private List<CalculatorDelayTask> delayTasks = new ArrayList<>();

    public void addDelayTasks(Collection<CalculatorDelayTask> collection) {
        this.delayTasks.addAll(collection);
    }

    @PostLoad
    public void postLoad() {
        this.target = Jackson.read(this.targetToast, TargetUserDesAdapter.class);
    }

    /**
     * 发布保存事件
     * @return
     */
    @DomainEvents
    public List<OrderInfoAsyncEvent> domainEvents() {
        return List.of(new OrderInfoAsyncEvent(this.orderId, OrderStatusEnum.of(this.status), this.version, this.target, this.delayTasks));
    }

}
