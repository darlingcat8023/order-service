package com.star.enterprise.order.refund.processor;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import com.star.enterprise.order.refund.model.OrderItemDelegate;
import com.star.enterprise.order.remote.course.RemoteCourseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xiaowenrou
 * @date 2023/3/2
 */
@Component
@AllArgsConstructor
public class MatchInfoPredicateProcessor implements OrderItemPredicateProcessor {

    private final RemoteCourseService remoteCourseService;

    @Override
    public boolean predicate(TargetUser target, OrderItemDelegate current, OrderSummaryTransObject orderObject) {
        var record = this.remoteCourseService.getMatchPriceById(current.getBusinessId(), target.campus());
        current.setCurrentStandardPrice(record.originalPrice());
        return true;
    }

}
