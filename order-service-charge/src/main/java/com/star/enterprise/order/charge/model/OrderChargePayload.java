package com.star.enterprise.order.charge.model;

import com.star.enterprise.order.charge.constants.BusinessTypeEnum;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/9/15
 */
public interface OrderChargePayload {

    /**
     * 业务类型
     * @return
     */
    BusinessTypeEnum type();

    /**
     * 业务id
     * @return
     */
    String businessId();

    /**
     * 收费类型
     * @return
     */
    String category();

    /**
     * single 属性
     * @return
     */
    List<ChargeProperties> properties();


}
