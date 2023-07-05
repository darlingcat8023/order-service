package com.star.enterprise.order.core.calculator;

/**
 * @author xiaowenrou
 * @date 2023/3/17
 */
public interface CouponDetail {

    /**
     * 优惠券模版id
     * @return
     */
    String templateId();

    /**
     * 优惠券编号
     * @return
     */
    String couponCode();

    /**
     * 计算顺序
     * @return
     */
    Integer order();

    /**
     * 优惠上下文
     * @return
     */
    CouponContext context();

}
