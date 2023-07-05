package com.star.enterprise.order.core.calculator;

import com.star.enterprise.order.core.handler.business.BusinessTypeHandler;
import com.star.enterprise.order.remote.course.response.SpecPriceRecord;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 费用上下文信息
 * @author xiaowenrou
 * @date 2022/9/26
 */
@Data
@Accessors(chain = true)
public final class OrderItemContext {

    private BusinessTypeHandler handler;

    private String orderItemId;

    private String productName;

    private SpecPriceRecord specRecord = SpecPriceRecord.empty();

    private String couponCalculateContextId;

}
