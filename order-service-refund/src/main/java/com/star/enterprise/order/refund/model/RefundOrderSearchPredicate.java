package com.star.enterprise.order.refund.model;

import com.star.enterprise.order.base.Paired;
import com.star.enterprise.order.core.model.PermissionQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RefundOrderSearchPredicate extends PermissionQuery {

    private String target;

    private Paired<Long, Long> timeRange;

    private String productName;

    private String refundStatus;

}
