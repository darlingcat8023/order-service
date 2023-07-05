package com.star.enterprise.order.remote.security.response;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/1/3
 */
public record RemoteTokenVerifyResponse(

        @JsonAlias(value = {"employee_id"})
        String employeeId,

        @JsonAlias(value = {"employee_name"})
        String employeeName,

        @JsonAlias(value = {"is_admin"})
        Boolean isAdmin,

        @JsonAlias(value = {"department_ids"})
        List<String> campusIds

) {

        public static RemoteTokenVerifyResponse empty() {
                return new RemoteTokenVerifyResponse(null, null, null, null);
        }

}
