package com.star.enterprise.order.charge.model.impl;

import com.star.enterprise.order.charge.model.ChargeProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaowenrou
 * @date 2022/9/19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultChargeProperties implements ChargeProperties {

    private String property;

    private String propertyDesc;

    private String standardValue;

    private String minValue;

    private String maxValue;

    private String attribute;

    private String penetrate;

}
