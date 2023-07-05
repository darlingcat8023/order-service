package com.star.enterprise.order.core.model.trans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/11/9
 */
@Data
@AllArgsConstructor
public class OrderDiscountPlanTransObject {

    private String discountPlanId;

    private String discountPlanName;

    private BigDecimal discountRate;

}
