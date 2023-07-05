package com.star.enterprise.order.receipt.data.es.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xiaowenrou
 * @date 2023/3/9
 */
@Data
@Accessors(chain = true)
public class SearchedAdditionalInfo {

    /**
     * 发票号
     */
    private String invoiceNo;

    /**
     * 内部备注
     */
    private String innerRemark;

    /**
     * 外部备注
     */
    private String outerRemark;

    /**
     * 原因
     */
    private String reason;

}
