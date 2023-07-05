package com.star.enterprise.order.receipt.model;

import com.star.enterprise.order.core.model.OrderSearchPredicate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author xiaowenrou
 * @date 2022/12/7
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ReceiptOrderSearchPredicate extends OrderSearchPredicate {

    // 收费类型
    private String chargeType;

    // 发票号
    private String invoiceNo;

    // 备注
    private String remark;

}
