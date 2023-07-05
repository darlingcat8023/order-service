package com.star.enterprise.order.http.order.response;

import com.star.enterprise.order.remote.course.response.SpecPriceRecord;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public record OrderItemResponseRecord(

        String businessType,

        String businessId,

        String productName,

        String specId,

        Map<String, Object> extendInfo,

        Integer number,

        boolean apportion,

        String webViewToast,

        OrderItemFeeResponseRecord feeRecord,

        SpecPriceRecord specContext

) {}
