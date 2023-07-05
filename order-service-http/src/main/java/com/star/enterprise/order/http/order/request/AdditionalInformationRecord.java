package com.star.enterprise.order.http.order.request;

import com.star.enterprise.order.core.model.AdditionalInformation;
import com.star.enterprise.order.core.model.PerformanceUser;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public record AdditionalInformationRecord(

        String invoiceNo,

        BigDecimal invoiceAmount,

        String innerRemark,

        String outerRemark,

        List<String> certs,

        List<PerformanceUser> performanceUser

) implements AdditionalInformation {

}
