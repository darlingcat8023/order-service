package com.star.enterprise.order.http.advice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaowenrou
 * @date 2023/4/26
 */
@Configuration(proxyBeanMethods = false)
public class SecurityAuditAspectConfiguration {

    @Bean
    public AbstractSecurityAuditAspect staticAspect() {
        return new DynamicSecurityAuditAspect();
    }

}
