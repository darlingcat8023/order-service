package com.star.enterprise.order.refund.processor;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import com.star.enterprise.order.refund.model.OrderItemDelegate;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/2
 */
public final class MatchPredicateProcessor implements OrderItemPredicateProcessor {

    private final List<OrderItemPredicateProcessor> processorList;

    public MatchPredicateProcessor(ApplicationContext context) {
        this(context, List.of(ArticleMatchLeftPredicateProcessor.class, MatchInfoPredicateProcessor.class));
    }

    public MatchPredicateProcessor(ApplicationContext context, List<Class<? extends OrderItemPredicateProcessor>> clazz) {
        this.processorList = new ArrayList<>();
        clazz.forEach(clz -> this.processorList.add(context.getBean(clz)));
    }

    @Override
    public boolean predicate(TargetUser target, OrderItemDelegate current, OrderSummaryTransObject orderObject) {
        return processorList.stream().allMatch(processor -> processor.predicate(target, current, orderObject));
    }

}
