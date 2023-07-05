package com.star.enterprise.order.http.order.response;

/**
 * @author xiaowenrou
 * @date 2023/3/21
 */
public record CouponDiscountRecord(

        String templateId,

        String templateName,

        String couponCode,

        Integer couponOrder

) {
}
