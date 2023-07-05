package com.star.enterprise.order.core.aspect;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xiaowenrou
 * @date 2023/2/3
 */
@Data
@AllArgsConstructor
public class DefaultAuditSequence implements AuditSequence {

    private final String business;

    private final String key;

    private final Object before;

    private final Object after;

    public static DefaultAuditSequence of(String business, String key, Object before, Object after) {
        return new DefaultAuditSequence(business, key, before, after);
    }

    @Override
    public String business() {
        return this.business;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public Object before() {
        return this.before;
    }

    @Override
    public Object after() {
        return this.after;
    }

}
