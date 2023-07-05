package com.star.enterprise.order.http.order.request;

import com.star.enterprise.order.core.calculator.DiscountPlanContext;
import com.star.enterprise.order.core.calculator.DiscountPlanDetail;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/17
 */
public record DiscountPlanRecord(

        String discountPlanId,

        BigDecimal afterDiscountSinglePrice,

        BigDecimal afterDiscountTotalPrice,

        BigDecimal discountRate,

        String discountPlanName,

        Integer apportion,

        DiscountPlanContext context

) implements DiscountPlanDetail {

    public DiscountPlanRecord(String discountPlanId, BigDecimal afterDiscountSinglePrice, BigDecimal afterDiscountTotalPrice, BigDecimal discountRate, String discountPlanName, Integer apportion,
                              DiscountPlanContext context) {
        this.discountPlanId = discountPlanId;
        this.afterDiscountSinglePrice = afterDiscountSinglePrice;
        this.afterDiscountTotalPrice = afterDiscountTotalPrice;
        this.discountRate = discountRate;
        this.discountPlanName = discountPlanName;
        this.apportion = apportion;
        this.context = new DiscountPlanContext().setDiscountPlanName(this.discountPlanName).setDiscountPlanRate(this.discountRate);
    }

}
