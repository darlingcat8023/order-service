package com.star.enterprise.order.transfer.processor;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import com.star.enterprise.order.refund.model.OrderItemDelegate;
import com.star.enterprise.order.refund.processor.OrderItemPredicateProcessor;
import com.star.enterprise.order.remote.course.RemoteCourseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CourseTransferInfoPredicateProcessor implements OrderItemPredicateProcessor {

    private final RemoteCourseService remoteCourseService;

    @Override
    public boolean predicate(TargetUser target, OrderItemDelegate current, OrderSummaryTransObject orderObject) {
        var record = this.remoteCourseService.getCoursePriceById(current.getBusinessId(), current.getSpecId(), target.campus());
        if (record.allowTransfer()) {
            current.setCurrentStandardPrice(record.standardPrice());
            log.info("predicate course info success, orderId = {}, orderItemId = {}", current.getOrderId(), current.getOrderItemId());
            return true;
        }
        log.info("predicate course info fail, orderId = {}, orderItemId = {}", current.getOrderId(), current.getOrderItemId());
        return false;
    }

}
