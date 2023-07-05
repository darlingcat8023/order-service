package com.star.enterprise.order.core.calculator;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xiaowenrou
 * @date 2023/3/17
 */
@Data
@Accessors(chain = true)
public class CouponContext {

    private String templateName;

}
