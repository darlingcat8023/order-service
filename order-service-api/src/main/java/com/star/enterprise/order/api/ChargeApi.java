package com.star.enterprise.order.api;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2022/9/16
 */
public interface ChargeApi {

    /**
     * 获取charge type列表
     * @return
     */
    Map<String, Object> chargeTypeMap();

}
