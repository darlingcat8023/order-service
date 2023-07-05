package com.star.enterprise.order.refund.model;

import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/2
 */
@Data
@RequiredArgsConstructor
public class OrderItemObject {

    private final OrderSummaryTransObject object;

    private final List<OrderItemDelegate> refundItems;

}
