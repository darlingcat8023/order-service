package com.star.enterprise.order.core.aspect;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;

/**
 * @author xiaowenrou
 * @date 2023/2/3
 */
public class AuditExpressionEvaluator {

    private final SpelExpressionParser parser = new SpelExpressionParser();

    /**
     * 构建表达式上下文
     * @param source
     * @param target
     * @param beanFactory
     * @return
     */
    public EvaluationContext createEvaluationContext(Object source, Object target, @Nullable BeanFactory beanFactory) {
        var evaluationContext = new StandardEvaluationContext(target);
        evaluationContext.setVariable("source", source);
        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        return evaluationContext;
    }

    protected ExpressionParser getParser() {
        return this.parser;
    }

    protected Expression getExpression(String expression) {
        return this.getParser().parseExpression(expression);
    }

    public String key(String keyExpression, EvaluationContext evalContext) {
        return String.valueOf(this.getExpression(keyExpression).getValue(evalContext));
    }

    public boolean condition(String conditionExpression, EvaluationContext evalContext) {
        return Boolean.TRUE.equals(this.getExpression(conditionExpression).getValue(evalContext, Boolean.class));
    }

}
