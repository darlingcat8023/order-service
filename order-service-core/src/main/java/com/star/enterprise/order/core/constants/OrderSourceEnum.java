package com.star.enterprise.order.core.constants;

import com.star.enterprise.order.base.EnumSerialize;
import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * 订单来源
 * @author xiaowenrou
 * @date 2022/9/26
 */
@AllArgsConstructor
public enum OrderSourceEnum implements EnumSerialize {

    FRONT_END("front_end", "前台订单")
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

    public static OrderSourceEnum of(String method) {
        return Arrays.stream(values()).filter(e -> e.value().equals(method)).findAny().orElse(null);
    }

}
