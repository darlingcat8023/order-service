package com.star.enterprise.order.core.aspect;

import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2023/2/3
 */
@Aspect
@Component
@AllArgsConstructor
public class AuditLogSequenceAspect {

    private final ApplicationContext applicationContext;

    private final AuditLogExecutor executor;

    private final AuditExpressionEvaluator expressionEvaluator = new AuditExpressionEvaluator();

    /**
     * 用来审计
     * @param joinPoint
     * @param source
     * @return
     * @throws Throwable
     */
    @Around(value = "execution(* org.springframework.data.jpa.repository.JpaRepository.saveAndFlush(*)) && args(source)")
    public Object around(ProceedingJoinPoint joinPoint, Object source) throws Throwable {
        var target = joinPoint.proceed(joinPoint.getArgs());
        var annotation = AnnotationUtils.findAnnotation(source.getClass(), AuditLog.class);
        if (annotation == null) {
            return target;
        }
        var ctx = this.expressionEvaluator.createEvaluationContext(source, target, this.applicationContext);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            this.doAuditWithTransaction(annotation, source, target, ctx);
        } else {
            this.doAuditWithNoTransaction(annotation, source, target, ctx);
        }
        return target;
    }

    /**
     * 未启用事务直接提交日志
     * @param auditLog
     * @param source
     * @param target
     * @param context
     */
    private void doAuditWithNoTransaction(AuditLog auditLog, Object source, Object target, EvaluationContext context) {
        this.buildAuditSequenceIfNeed(auditLog, source, target, context).map(List::of).ifPresent(this.executor::execute);
    }

    /**
     * 启用事务后先暂存，随事务一起提交日志
     * @param auditLog
     * @param source
     * @param target
     * @param context
     */
    private void doAuditWithTransaction(AuditLog auditLog, Object source, Object target, EvaluationContext context) {
        var transactionSynchronization = this.getCurrentSynchronization(TransactionSynchronizationManager.getSynchronizations());
        if (transactionSynchronization == null) {
            synchronized (this) {
                transactionSynchronization = this.getCurrentSynchronization(TransactionSynchronizationManager.getSynchronizations());
                if (transactionSynchronization == null) {
                    transactionSynchronization = new AuditLogTransactionSynchronization(this.executor);
                    TransactionSynchronizationManager.registerSynchronization(transactionSynchronization);
                }
            }
        }
        this.buildAuditSequenceIfNeed(auditLog, source, target, context).ifPresent(transactionSynchronization::addSequence);
    }

    private Optional<AuditSequence> buildAuditSequenceIfNeed(AuditLog auditLog, Object source, Object target, EvaluationContext context) {
        if (!this.conditionPass(auditLog, context)) {
            return Optional.empty();
        }
        var key = this.keyPass(auditLog, context);
        return Optional.of(DefaultAuditSequence.of(null, key, source, target));
    }

    private boolean conditionPass(AuditLog auditLog, EvaluationContext evaluationContext) {
        String condition = auditLog.condition();
        if (StringUtils.hasText(condition)) {
            return this.expressionEvaluator.condition(condition, evaluationContext);
        }
        return true;
    }

    private String keyPass(AuditLog auditLog, EvaluationContext evaluationContext) {
        return this.expressionEvaluator.key(auditLog.key(), evaluationContext);
    }

    private AuditLogTransactionSynchronization getCurrentSynchronization(List<TransactionSynchronization> synchronizationList) {
        for (var syn : synchronizationList) {
            if (syn instanceof AuditLogTransactionSynchronization alt) {
                return alt;
            }
        }
        return null;
    }

}
