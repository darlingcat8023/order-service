package com.star.enterprise.order.refund.processor;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import com.star.enterprise.order.core.service.OrderItemService;
import com.star.enterprise.order.refund.model.OrderItemDelegate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/4/3
 */
@Component
@AllArgsConstructor
public class ArticleMatchLeftPredicateProcessor implements OrderItemPredicateProcessor {

    private final OrderItemService itemService;

    @Override
    public boolean predicate(TargetUser target, OrderItemDelegate current, OrderSummaryTransObject orderObject) {
        return this.itemService.getOrderItemLeft(current.getOrderItemId()).map(item -> {
            var total = item.getNumberLeft().add(item.getApportionLeft());
            if (total.compareTo(BigDecimal.ZERO) > 0) {
                current.setNumberLeft(item.getNumberLeft());
                current.setApportionLeft(item.getApportionLeft());
                current.setVersion(item.getVersion());
                return true;
            }
            return false;
        }).orElse(false);
    }

}
