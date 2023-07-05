package com.star.enterprise.order.core.aspect;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/2/3
 */
@Component
@AllArgsConstructor
public class RedisAuditExecutor implements AuditLogExecutor {

    @Override
    public void execute(List<AuditSequence> list) {
        list.forEach(System.out::println);
    }

}
