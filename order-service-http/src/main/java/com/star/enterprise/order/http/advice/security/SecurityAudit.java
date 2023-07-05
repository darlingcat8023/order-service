package com.star.enterprise.order.http.advice.security;

import java.lang.annotation.*;

/**
 * 安全校验注解
 * @author xiaowenrou
 * @date 2023/1/4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface SecurityAudit {

}
