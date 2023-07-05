package com.star.enterprise.order.core.aspect;

import org.springframework.transaction.support.TransactionSynchronization;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xiaowenrou
 * @date 2023/2/3
 */
public final class AuditLogTransactionSynchronization implements TransactionSynchronization {

    private final List<AuditSequence> auditSequenceList = new CopyOnWriteArrayList<>();

    private final AuditLogExecutor auditLogExecutor;

    public AuditLogTransactionSynchronization(AuditLogExecutor executor) {
        this.auditLogExecutor = executor;
    }

    @Override
    public void afterCommit() {
        try {
            this.auditLogExecutor.execute(this.auditSequenceList);
            TransactionSynchronization.super.afterCommit();
        } finally {
            this.auditSequenceList.clear();
        }
    }

    public void addSequence(AuditSequence sequence) {
        if (sequence != null) {
            this.auditSequenceList.add(sequence);
        }
    }

}
