package com.star.enterprise.order.remote.course.response;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * @author xiaowenrou
 * @date 2023/3/2
 */
public record CourseRefundRecord(

        @JsonAlias(value = "allow_refund")
        boolean allowRefund,

        @JsonAlias(value = "allow_transfer")
        boolean allowTransfer

) {
}
