package com.star.enterprise.order.core.calculator.holder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;

/**
 * @author xiaowenrou
 * @date 2023/3/17
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public final class DelegatingAccumulateHolder implements AccumulateHolder {

    @Delegate
    private AccumulateHolder delegate;

    public static DelegatingAccumulateHolder newInstance() {
        return new DelegatingAccumulateHolder(new DefaultAccumulateHolder());
    }

}
