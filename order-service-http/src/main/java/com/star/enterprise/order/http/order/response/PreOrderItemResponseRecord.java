package com.star.enterprise.order.http.order.response;

import com.star.enterprise.order.remote.course.response.SpecPriceRecord;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public record PreOrderItemResponseRecord(

        String businessType,

        String businessId,

        String specId,

        Map<String, Object> extendInfo,

        Integer number,

        BigDecimal originalPrice,

        BigDecimal originalTotalPrice,

        DiscountPlanResponseRecord discountPlanRecord,

        BigDecimal afterDiscountSinglePrice,

        BigDecimal afterDiscountTotalPrice,

        boolean apportion,

        String webViewToast,

        SpecPriceRecord specContext

) {}
