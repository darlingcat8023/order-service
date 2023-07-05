package com.star.enterprise.order.core.data.jpa.projection;

import java.math.BigDecimal;

/**
 * A Projection for the {@link com.star.enterprise.order.core.data.jpa.entity.OrderItemLeftEntity} entity
 */
public interface OrderItemLeftProjection {
    String getOrderItemId();

    Integer getVersion();

    String getBusinessId();

    String getBusinessType();

    BigDecimal getNumberLeft();

    BigDecimal getApportionLeft();
}