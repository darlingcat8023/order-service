package com.star.enterprise.order.core.model.trans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xiaowenrou
 * @date 2022/12/19
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class OrderCouponTransObject {

    private String templateId;

    private String templateName;

    private String couponCode;

    private Integer couponOrder;

}
