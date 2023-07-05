package com.star.enterprise.order.charge.constants;

import com.star.enterprise.order.base.EnumSerialize;
import com.star.enterprise.order.charge.model.Fee;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author xiaowenrou
 * @date 2022/9/19
 */
@AllArgsConstructor
public enum PriceAttributeEnum implements EnumSerialize {

    /**
     * value
     */
    ORIGINAL("original", "应交金额") {
        @Override
        public BigDecimal targetValue(Fee fee) {
            return fee.getOriginalTotalPrice();
        }
    },

    DUE_COLLECT("due_collect", "实交金额") {
        @Override
        public BigDecimal targetValue(Fee fee) {
            return fee.getDueCollectPrice();
        }
    },

    AFTER_DISCOUNT("after_discount", "折后金额") {
        @Override
        public BigDecimal targetValue(Fee fee) {
            return fee.getAfterDiscountTotalPrice();
        }
    },

    NONE("none", "无门槛") {
        @Override
        public BigDecimal targetValue(Fee fee) {
            throw new UnsupportedOperationException("not support");
        }
    }
    ;

    private final String value;

    private final String desc;

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public String desc() {
        return this.desc;
    }

    public static PriceAttributeEnum of(String category) {
        return Arrays.stream(values()).filter(item -> item.value.equals(category)).findFirst().orElse(null);
    }

    /**
     * 待计算的值
     * @param fee
     * @return
     */
    public abstract BigDecimal targetValue(Fee fee);

}
