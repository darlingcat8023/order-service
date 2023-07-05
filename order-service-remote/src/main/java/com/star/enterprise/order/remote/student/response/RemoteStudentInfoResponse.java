package com.star.enterprise.order.remote.student.response;

import com.fasterxml.jackson.annotation.JsonAlias;
/**
 * @author xiaowenrou
 * @date 2022/11/1
 */
public record RemoteStudentInfoResponse(

        @JsonAlias(value = {"student_id"})
        String userId,

        @JsonAlias(value = {"student_name"})
        String userName,

        @JsonAlias(value = {"student_mobile"})
        String userMobile,

        @JsonAlias(value = {"student_number"})
        String userNumber,

        @JsonAlias(value = {"advisor_id"})
        String advisorId,
        
        @JsonAlias(value = {"human_id"})
        String humanId,
        
        @JsonAlias(value = {"app_name"})
        String appName,
        @JsonAlias(value = {"app_bind_type"})
         String appBindType,
        @JsonAlias(value = {"student_birth_day"})
        String studentBirthDay,
        @JsonAlias(value = {"student_gender"})
        String studentGender,
        @JsonAlias(value = {"customer_id"})
        String customerId,
        @JsonAlias(value = {"organization_id"})
        String organizationId,
        @JsonAlias(value = {"department_id"})
        String departmentId,
        @JsonAlias(value = {"department_name"})
        String departmentName,
        @JsonAlias(value = {"level_id"})
        String levelId,
        @JsonAlias(value = {"student_type"})
        String studentType,
        @JsonAlias(value = {"student_category"})
        String studentCategory,
        @JsonAlias(value = {"student_cash"})
        Integer studentCash,
        @JsonAlias(value = {"father_name"})
        String fatherName,
        @JsonAlias(value = {"father_mobile"})
        String fatherMobile,
        @JsonAlias(value = {"father_job"})
        String fatherJob,
        @JsonAlias(value = {"mother_name"})
        String motherName,
        @JsonAlias(value = {"mother_mobile"})
        String motherMobile,
        @JsonAlias(value = {"mother_job"})
        String motherJob,
        @JsonAlias(value = {"other_mobile"})
        String otherMobile,
        @JsonAlias(value = {"home_address"})
        String homeAddress,
        @JsonAlias(value = {"channel_id"})
        String channelId,
        @JsonAlias(value = "channel_name")
        String channelName,
        @JsonAlias(value = {"sub_channel_id"})
        String subChannelId,
        @JsonAlias(value = "sub_channel_name")
        String subChannelName,
        @JsonAlias(value = {"advisor_id"})
        String advisorIdX

) {}
