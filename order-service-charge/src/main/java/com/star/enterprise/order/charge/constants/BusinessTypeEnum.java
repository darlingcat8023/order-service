package com.star.enterprise.order.charge.constants;

import com.star.enterprise.order.base.EnumSerialize;
import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * @author xiaowenrou
 * @date 2022/9/20
 */
@AllArgsConstructor
public enum BusinessTypeEnum implements EnumSerialize {

    /**
     * value
     */
    COURSE("course", "课程"),
    ARTICLE("article", "物品"),
    MATCH("match", "赛事"),
    WALLET("wallet", "钱包")
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

    /**
     * 获取枚举值
     * @param business
     * @return
     */
    public static BusinessTypeEnum of(String business) {
        return Arrays.stream(values()).filter(e -> e.value().equals(business)).findAny().orElse(null);
    }

}
