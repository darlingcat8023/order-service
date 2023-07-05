package com.star.enterprise.order.charge.model;

import com.star.enterprise.order.charge.constants.PriceAttributeEnum;

/**
 * @author xiaowenrou
 * @date 2022/9/19
 */
public interface PriceInfo {

    /**
     * 类型枚举
     * @return
     */
    PriceAttributeEnum attributeEnum();

    /**
     * 标准值
     * @return
     */
    String standard();

    /**
     * 范围值 - min
     * @return
     */
    String min();

    /**
     * 范围值 - max
     * @return
     */
    String max();

    /**
     * 转换properties
     * @return
     */
    ChargeProperties convertChargeProperties();

}
