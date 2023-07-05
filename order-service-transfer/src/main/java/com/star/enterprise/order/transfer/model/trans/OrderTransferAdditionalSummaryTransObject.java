package com.star.enterprise.order.transfer.model.trans;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferAdditionInfoEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Data
@Accessors(chain = true)
public class OrderTransferAdditionalSummaryTransObject {

    /**
     * 备注
     */
    private String remark;

    /**
     * 退款原因
     */
    private String transferReason;

    /**
     * 附件列表
     */
    @JsonRawValue
    private String file;

    public OrderTransferAdditionalSummaryTransObject(OrderTransferAdditionInfoEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }

}
