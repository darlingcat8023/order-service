package com.star.enterprise.order.http.refund.request;

import com.star.enterprise.order.http.refund.VerifyGroup;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public class RefundExtendGroupSequenceProvider implements DefaultGroupSequenceProvider<OrderRefundExtendRecord> {

    @Override
    public List<Class<?>> getValidationGroups(OrderRefundExtendRecord record) {
        var groups = new ArrayList<Class<?>>();
        groups.add(OrderRefundExtendRecord.class);
        if (record != null && "transfer".equals(record.refundMethod())) {
            groups.add(VerifyGroup.ValidWithCard.class);
        }
        return groups;
    }

}
