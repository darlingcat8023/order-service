package com.star.enterprise.order.transfer.processor;

import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import com.star.enterprise.order.refund.model.OrderItemDelegate;
import com.star.enterprise.order.refund.processor.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DelegatingTransferPredicateProcessor implements OrderItemPredicateProcessor {

    private final OrderItemPredicateProcessor delegate;

    public static OrderItemPredicateProcessor build(ApplicationContext context, BusinessTypeEnum businessType) {
        return build(context, businessType, true, true);
    }

    public static OrderItemPredicateProcessor build(ApplicationContext context, BusinessTypeEnum businessType, boolean containsMatch, boolean containsArticle) {
        var delegate = switch (businessType) {
            case COURSE -> {
                var customizer = List.of(CourseTransferInfoPredicateProcessor.class, CourseBalancePredicateProcessor.class);
                yield new CoursePredicateProcessor(context, customizer);
            }
            case ARTICLE -> {
                var customizer = List.of(ArticleMatchLeftPredicateProcessor.class, ArticleTransferInfoPredicateProcessor.class);
                yield containsArticle ? new ArticlePredicateProcessor(context, customizer) : OrderItemPredicateProcessor.FALSE;
            }
            case MATCH -> containsMatch ? new MatchPredicateProcessor(context) : OrderItemPredicateProcessor.FALSE;
            case WALLET -> OrderItemPredicateProcessor.FALSE;
        };
        return new DelegatingTransferPredicateProcessor(delegate);
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
