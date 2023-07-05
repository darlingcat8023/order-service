package com.star.enterprise.order.http.order.request;

import com.star.enterprise.order.core.calculator.CouponContext;
import com.star.enterprise.order.core.calculator.CouponDetail;

/**
 * @author xiaowenrou
 * @date 2023/3/17
 */
public record CouponRecord(

        String templateId,

        String couponCode,

        Integer order,

        String templateName,

        CouponContext context

) implements CouponDetail {

    public CouponRecord(String templateId, String couponCode, Integer order, String templateName, CouponContext context) {
        this.templateId = templateId;
        this.couponCode = couponCode;
        this.order = order;
        this.templateName = templateName;
        this.context = new CouponContext().setTemplateName(templateName);
    }
}
