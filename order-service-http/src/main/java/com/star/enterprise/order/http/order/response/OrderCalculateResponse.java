package com.star.enterprise.order.http.order.response;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public record OrderCalculateResponse(

        String webViewToast,

        DiscountInfoResponseRecord discountInfo,

        List<OrderItemResponseRecord> items,

        OrderFeeResponseRecord orderFeeRecord

) {}
