package com.star.enterprise.order.http.transfer.request;

import com.star.enterprise.order.transfer.model.TransferExtendInfo;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public record OrderTransferExtendRecord(

        String remark,

        String transferReason,

        List<String> files

) implements TransferExtendInfo {}
