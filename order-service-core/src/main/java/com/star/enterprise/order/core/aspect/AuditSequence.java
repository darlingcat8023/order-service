package com.star.enterprise.order.core.aspect;

/**
 * @author xiaowenrou
 * @date 2023/2/3
 */
public interface AuditSequence {

    String business();

    String key();

    Object before();

    Object after();

}
