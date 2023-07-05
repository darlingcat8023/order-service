package com.star.enterprise.order.core.model;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/12/29
 */
public interface OrderExtendInfo {

    /**
     * 付款方式详情
     * @return
     */
    List<PaymentInfo> payments();


    /**
     * 业绩信息
     * @return
     */
    AdditionalInformation invoice();

}
