package com.star.enterprise.order.http.advice.security;

import org.aspectj.lang.annotation.SuppressAjWarnings;

/**
 * aspectj静态代理，需要ajc编译
 * @author xiaowenrou
 * @date 2023/2/2
 */
public aspect StaticSecurityAuditAspect extends AbstractSecurityAuditAspect {

    @SuppressAjWarnings(value = {"adviceDidNotMatch"})
    public Object around(final SecurityAudit securityAudit) : @annotation(securityAudit) || @within(securityAudit) {
        try {
            return super.execute(() -> proceed(securityAudit));
        } catch (Throwable e) {
            return null;
        }
    }

}
