package com.star.enterprise.order.refund.model.trans;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundInfoEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class OrderRefundSummaryTransObject {

    private final String refundOrderId;

    /**
     * 审批id
     */
    private String approvalId;

    /**
     * 创建日期
     */
    private LocalDateTime createdDate;

    /**
     * 更新日期
     */
    private LocalDateTime lastModifyDate;

    /**
     * 操作人
     */
    @JsonRawValue
    private String operator;

    /**
     * 退费状态
     */
    private String refundStatus;

    private OrderRefundFeeSummaryTransObject refundFee;

    private List<OrderRefundItemSummaryTransObject> items;

    private OrderRefundAdditionalSummaryTransObject additional;

    /**
     * 页面透传
     */
    private String webViewToast;

    /**
     * 用户信息
     */
    private TargetUser target;

    public OrderRefundSummaryTransObject(OrderRefundInfoEntity entity) {
        this(entity.getRefundOrderId());
        BeanUtils.copyProperties(entity, this);
        this.target = entity.getTarget();
    }

}
