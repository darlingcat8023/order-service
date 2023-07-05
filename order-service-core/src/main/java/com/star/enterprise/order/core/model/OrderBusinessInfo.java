package com.star.enterprise.order.core.model;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public interface OrderBusinessInfo {

    /**
     * 业务类型
     * @return
     */
    String businessType();

    /**
     * 业务id
     * @return
     */
    String businessId();

    /**
     * 业务规格
     * @return
     */
    String specId();

    /**
     * 其他信息
     * @return
     */
    Map<String, Object> extendInfo();

    /**
     * 透传字段
     * @return
     */
    String webViewToast();

}
