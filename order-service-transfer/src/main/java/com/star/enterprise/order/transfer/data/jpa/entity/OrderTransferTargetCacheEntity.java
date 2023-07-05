package com.star.enterprise.order.transfer.data.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import static com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferTargetCacheEntity.TABLE_NAME;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = TABLE_NAME, indexes = {
        @Index(name = "idx_ottce_tsid_taid_caid", columnList = "transferOrderId, targetId, targetCampusId")
})
@EntityListeners(value = {AuditingEntityListener.class})
public class OrderTransferTargetCacheEntity {

    protected static final String TABLE_NAME = "order_transfer_target_cache";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = TABLE_NAME + "_gen")
    @SequenceGenerator(name = TABLE_NAME + "_gen", sequenceName = TABLE_NAME+"_seq", allocationSize = 1)
    private Long id;

    private String transferOrderId;

    /**
     * 目标用户id
     */
    private String targetId;

    /**
     * 目标用户名
     */
    private String targetName;

    /**
     * 目标用户校区
     */
    private String targetCampusId;

    private String targetCampusName;

    /**
     * 目标用户状态
     */
    private String targetStatus;

    /**
     * 目标用户手机号
     */
    private String targetMobile;

    /**
     * 用户学号
     */
    private String targetNumber;

    /**
     * 一级渠道
     */
    private String channelId;

    private String channelName;

    /**
     * 二级渠道
     */
    private String subChannelId;

    private String subChannelName;

    /**
     * 用户透传字段
     */
    @Column(columnDefinition = "text")
    private String userToast;

}
