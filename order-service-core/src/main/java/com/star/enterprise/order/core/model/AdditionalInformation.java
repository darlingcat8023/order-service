package com.star.enterprise.order.core.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/9/22
 */
public interface AdditionalInformation {

    /**
     * 发票编号
     * @return
     */
    String invoiceNo();

    /**
     * 发票金额
     * @return
     */
    BigDecimal invoiceAmount();

    /**
     * 发票地址
     * @return
     */
    List<String> certs();

    /**
     * 内部备注
     * @return
     */
    String innerRemark();

    /**
     * 外部备注
     * @return
     */
    String outerRemark();


    /**
     * 业绩归属人
     * @return
     */
    List<PerformanceUser> performanceUser();

}
