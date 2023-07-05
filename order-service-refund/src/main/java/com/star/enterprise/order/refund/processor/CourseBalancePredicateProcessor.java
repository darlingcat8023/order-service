package com.star.enterprise.order.refund.processor;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import com.star.enterprise.order.refund.model.OrderItemDelegate;
import com.star.enterprise.order.remote.wallet.RemoteWalletCourseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/2
 */
@Slf4j
@Component
@AllArgsConstructor
public class CourseBalancePredicateProcessor implements OrderItemPredicateProcessor {

    private final RemoteWalletCourseService walletCourseService;

    @Override
    public boolean predicate(TargetUser target, OrderItemDelegate current, OrderSummaryTransObject orderObject) {
        var record = this.walletCourseService.walletCourseDetail(current.getOrderItemId());
        log.info("record = {}", record.toString());
        log.info("walletId = {}, totalLeft = {}, predicate = {}", record.walletId(), record.totalLeft(), StringUtils.hasText(record.walletId()) && BigDecimal.ZERO.compareTo(record.totalLeft()) < 0);
        if (StringUtils.hasText(record.walletId()) && BigDecimal.ZERO.compareTo(record.totalLeft()) < 0) {
            current.setNumberLeft(record.numberLeft());
            current.setApportionLeft(record.apportionLeft());
            log.info("predicate course balance success, orderId = {}, orderItemId = {}", current.getOrderId(), current.getOrderItemId());
            return true;
        }
        log.info("predicate course balance fail, orderId = {}, orderItemId = {}", current.getOrderId(), current.getOrderItemId());
        return false;
    }

}
