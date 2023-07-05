package com.star.enterprise.order.http.advice.security;

import com.star.enterprise.order.core.model.EmployeeUser;
import org.springframework.core.NamedThreadLocal;

import java.util.Objects;
import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2023/1/4
 */
public class WebSecurityContext {

    private static final ThreadLocal<EmployeeUser> SECURITY = new NamedThreadLocal<>("web-security");

    public static EmployeeUser setSecurity(EmployeeUser employee) {
        assert Objects.nonNull(employee);
        WebSecurityContext.SECURITY.set(employee);
        return employee;
    }

    public static Optional<EmployeeUser> getSecurity() {
        return Optional.ofNullable(WebSecurityContext.SECURITY.get());
    }

    public static void removeSecurity() {
        WebSecurityContext.SECURITY.remove();
    }

}
