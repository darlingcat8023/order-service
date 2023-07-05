package com.star.enterprise.order.charge.model.impl;

import com.star.enterprise.order.charge.data.jpa.entity.ChargeItemEntity;
import com.star.enterprise.order.charge.model.ChargeProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/9/20
 */
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class DefaultChargeItemDetail implements Serializable {

    @Delegate
    private transient final ChargeItemEntity entity;

    private transient List<ChargeProperties> properties;

}
