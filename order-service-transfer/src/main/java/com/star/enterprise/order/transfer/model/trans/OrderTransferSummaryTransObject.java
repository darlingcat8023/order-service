package com.star.enterprise.order.transfer.model.trans;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferInfoEntity;
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
public class OrderTransferSummaryTransObject {

    private final String transferOrderId;

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
    private String transferStatus;

    private OrderTransferFeeSummaryTransObject transferFee;

    private List<OrderTransferItemSummaryTransObject> items;

    private OrderTransferAdditionalSummaryTransObject additional;

    /**
     * 页面透传
     */
    private String webViewToast;

    /**
     * 用户信息
     */
    private TargetUser target;

    public OrderTransferSummaryTransObject(OrderTransferInfoEntity entity) {
        this(entity.getTransferOrderId());
        BeanUtils.copyProperties(entity, this);
        this.target = entity.getTarget();
    }

}
