package com.star.enterprise.order.core.adapter;

import com.star.enterprise.order.core.model.EmployeeUser;
import lombok.Data;

/**
 * @author xiaowenrou
 * @date 2023/1/3
 */
@Data
public class EmployeeUserAdapter implements EmployeeUser {

    private final String employeeId;

    private final String employeeName;

    private final Boolean isAdmin;

    public EmployeeUserAdapter(String employeeId, String employeeName) {
        this(employeeId, employeeName, null);
    }

    public EmployeeUserAdapter(String employeeId, String employeeName, Boolean isAdmin) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.isAdmin = isAdmin;
    }

    @Override
    public String employeeId() {
        return this.employeeId;
    }

    @Override
    public String employeeName() {
        return this.employeeName;
    }

    @Override
    public Boolean isAdmin() {
        return this.isAdmin;
    }
}
