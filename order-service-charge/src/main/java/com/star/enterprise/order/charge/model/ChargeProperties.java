package com.star.enterprise.order.charge.model;

/**
 * @author xiaowenrou
 * @date 2022/9/19
 */
public interface ChargeProperties {

    /**
     * 属性字段
     * @return
     */
    String getProperty();

    /**
     * 属性名
     * @return
     */
    String getPropertyDesc();

    /**
     * 标准值
     * @return
     */
    String getStandardValue();

    /**
     * 范围值最小
     * @return
     */
    String getMinValue();

    /**
     * 范围值最大
     * @return
     */
    String getMaxValue();

    /**
     * 属性 attr
     * @return
     */
    String getAttribute();

    /**
     * 透传字段
     * @return
     */
    String getPenetrate();

}
