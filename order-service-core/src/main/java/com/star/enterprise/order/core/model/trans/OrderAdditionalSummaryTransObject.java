package com.star.enterprise.order.core.model.trans;

import com.star.enterprise.order.core.model.PerformanceUser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/10/27
 */
@Data
@Accessors(chain = true)
public class OrderAdditionalSummaryTransObject {

    private String invoiceNo;

    private BigDecimal invoiceAmount;

    private String innerRemark;

    private String outerRemark;

    List<String> certs;

    List<PerformanceUser> performanceUser;

}
