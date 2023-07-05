package com.star.enterprise.order.core.aspect;

import java.lang.annotation.*;

/**
 * @author xiaowenrou
 * @date 2023/2/3
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface AuditLog {

    /**
     * 比较
     */
    String condition() default "";

    /**
     * 主键
     */
    String key();

}
