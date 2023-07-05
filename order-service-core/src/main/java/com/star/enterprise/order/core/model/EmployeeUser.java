package com.star.enterprise.order.core.model;

/**
 * @author xiaowenrou
 * @date 2023/1/3
 */
public interface EmployeeUser {

    /**
     * 企业用户id
     * @return
     */
    String employeeId();

    /**
     * 企业用户名
     * @return
     */
    String employeeName();

    /**
     * 管理员
     * @return
     */
    Boolean isAdmin();

}
