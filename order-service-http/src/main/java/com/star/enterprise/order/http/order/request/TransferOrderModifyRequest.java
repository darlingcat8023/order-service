package com.star.enterprise.order.http.order.request;

import com.star.enterprise.order.transfer.model.TransferExtendInfo;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/14
 */
public record TransferOrderModifyRequest(

        String remark,

        String transferReason,

        List<String> files


) implements TransferExtendInfo {}
