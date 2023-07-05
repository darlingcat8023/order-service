package com.star.enterprise.order.core.calculator;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/17
 */
public interface DiscountPlanDetail {

    /**
     * 优惠方案id
     * @return
     */
    String discountPlanId();

    /**
     * 折后单价
     * @return
     */
    BigDecimal afterDiscountSinglePrice();

    /**
     * 折后总价
     * @return
     */
    BigDecimal afterDiscountTotalPrice();

    /**
     * 赠送数量
     * @return
     */
    Integer apportion();

    /**
     * 折扣方案上下文
     * @return
     */
    DiscountPlanContext context();

}
