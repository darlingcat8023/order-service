package com.star.enterprise.order.http.advice.security;

import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * aspectj动态代理
 * @author xiaowenrou
 * @date 2023/1/4
 */
@Aspect
@AllArgsConstructor
public class DynamicSecurityAuditAspect extends AbstractSecurityAuditAspect {

    /**
     * 环绕通知，匹配 @SecurityAudit 方法
     * @param point
     * @param securityAudit
     * @return
     * @throws Throwable
     */
    @Around(value = "@annotation(securityAudit) || @within(securityAudit)")
    public Object aroundExecution(ProceedingJoinPoint point, SecurityAudit securityAudit) throws Throwable {
       return super.execute(() -> point.proceed(point.getArgs()));
    }

}
