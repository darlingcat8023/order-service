package com.star.enterprise.order.core.aspect;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/2/3
 */
public interface AuditLogExecutor {

    void execute(List<AuditSequence> list);

}
