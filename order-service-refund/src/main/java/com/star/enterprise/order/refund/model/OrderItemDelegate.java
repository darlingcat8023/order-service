package com.star.enterprise.order.refund.model;

import com.star.enterprise.order.core.model.trans.OrderItemSummaryTransObject;
import lombok.Data;
import lombok.experimental.Delegate;

import java.math.BigDecimal;

/**
 * 包装类
 * @author xiaowenrou
 * @date 2023/3/2
 */
@Data
public class OrderItemDelegate {

    @Delegate
    private final OrderItemSummaryTransObject delegate;

    private BigDecimal numberLeft;

    private BigDecimal apportionLeft;

    private BigDecimal currentStandardPrice;

    private Integer version;

    public OrderItemDelegate(OrderItemSummaryTransObject object) {
        this.delegate = object;
        this.numberLeft = new BigDecimal(object.getNumber());
        this.apportionLeft = new BigDecimal(object.getApportion());
    }

}
