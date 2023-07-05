package com.star.enterprise.order.refund.processor;

import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import com.star.enterprise.order.refund.model.OrderItemDelegate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

/**
 * @author xiaowenrou
 * @date 2023/3/2
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DelegatingPredicateProcessor implements OrderItemPredicateProcessor {

    private final OrderItemPredicateProcessor delegate;

    public static OrderItemPredicateProcessor build(ApplicationContext context, BusinessTypeEnum businessType) {
        return build(context, businessType, true, true);
    }

    public static OrderItemPredicateProcessor build(ApplicationContext context, BusinessTypeEnum businessType, boolean containsMatch, boolean containsArticle) {
        var delegate = switch (businessType) {
            case COURSE -> new CoursePredicateProcessor(context);
            case MATCH -> containsMatch ? new MatchPredicateProcessor(context) : OrderItemPredicateProcessor.FALSE;
            case ARTICLE -> containsArticle ? new ArticlePredicateProcessor(context) : OrderItemPredicateProcessor.FALSE;
            case WALLET -> OrderItemPredicateProcessor.TRUE;
        };
        return new DelegatingPredicateProcessor(delegate);
    }

    @Override
    public boolean predicate(TargetUser target, OrderItemDelegate current, OrderSummaryTransObject orderObject) {
        return this.delegate.predicate(target, current, orderObject);
    }

    @Override
    public boolean predicate(TargetUser target, OrderItemDelegate current) {
        return this.delegate.predicate(target, current);
    }
}
