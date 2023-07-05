package com.star.enterprise.order.remote.student.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * @author xiaowenrou
 * @date 2022/11/2
 */
public record RemoteStudentInfoRequest(

        @JsonProperty(value = "student_id", access = READ_ONLY)
        String studentId,

        @JsonProperty(value = "campus_id", access = READ_ONLY)
        String campusId

) {}
