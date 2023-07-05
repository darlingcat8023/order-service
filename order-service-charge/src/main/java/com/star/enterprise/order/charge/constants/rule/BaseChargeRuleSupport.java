package com.star.enterprise.order.charge.constants.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaowenrou
 * @date 2022/9/19
 */
@Getter
@AllArgsConstructor
public enum BaseChargeRuleSupport {

    /**
     * value
     */
    LIMIT("limit", "限制触发次数"),
    AVAILABLE_TIME("availableTime", "有效期"),
    PRICE_ATTR("priceAttr", "费用类型"),
    CAMPUS("campusId", "授权校区")
    ;

    private final String property;

    private final String propertyDesc;

}
