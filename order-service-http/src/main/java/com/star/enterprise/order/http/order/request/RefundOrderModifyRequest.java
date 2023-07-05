package com.star.enterprise.order.http.order.request;

import com.star.enterprise.order.refund.model.RefundExtendInfo;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/14
 */
public record RefundOrderModifyRequest(

        String refundMethod,

        String bankAccount,

        String bankId,

        String bankName,

        String cardNumber,

        String remark,

        String refundReason,

        List<String> files

) implements RefundExtendInfo {}
