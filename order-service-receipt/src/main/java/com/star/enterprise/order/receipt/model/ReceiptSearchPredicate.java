package com.star.enterprise.order.receipt.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/12/7
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ReceiptSearchPredicate extends ReceiptOrderSearchPredicate {

    // 收据类型
    private List<String> receiptType;

    // 收据号
    private String receiptNo;

    // 收据状态
    private String status;

    // 经手人
    private String operator;

}
