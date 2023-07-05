package com.star.enterprise.order.charge.constants.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaowenrou
 * @date 2022/9/19
 */
@Getter
@AllArgsConstructor
public enum CourseChargeRuleSupport {

    /**
     * value
     */
    COURSE_SPEC("courseSpecId", "课程规格"),
    EXECUTOR_STATUS("executorStatus", "学员状态"),
    ;

    private final String property;

    private final String propertyDesc;

}
