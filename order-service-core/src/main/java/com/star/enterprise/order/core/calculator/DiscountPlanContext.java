package com.star.enterprise.order.core.calculator;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/17
 */
@Data
@Accessors(chain = true)
public class DiscountPlanContext {

    private String discountPlanName;

    private BigDecimal discountPlanRate;

}
