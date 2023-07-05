package com.star.enterprise.order.refund.model.trans;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundAdditionInfoEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Data
@Accessors(chain = true)
public class OrderRefundAdditionalSummaryTransObject {

    /**
     * 退款方式
     */
    private String refundMethod;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 银行id
     */
    private String bankId;

    /**
     * 银行名
     */
    private String bankName;

    /**
     * 银行账号
     */
    private String cardNumber;

    /**
     * 备注
     */
    private String remark;

    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 附件列表
     */
    @JsonRawValue
    private String file;

    public OrderRefundAdditionalSummaryTransObject(OrderRefundAdditionInfoEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }

}
