package com.star.enterprise.order.http.order.response;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public record PreCalculateResponse(

        String webViewToast,

        List<PreOrderItemResponseRecord> records

) {}
