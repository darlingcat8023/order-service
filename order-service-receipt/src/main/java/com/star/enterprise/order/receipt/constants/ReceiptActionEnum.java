package com.star.enterprise.order.receipt.constants;

import com.star.enterprise.order.base.EnumSerialize;
import lombok.AllArgsConstructor;

/**
 * @author xiaowenrou
 * @date 2022/12/6
 */
@AllArgsConstructor
public enum ReceiptActionEnum implements EnumSerialize {

    // 创建
    CREATE("create", "创建"),
    // 收费
    CHARGE("charge", "收费"),
    // 修改
    MODIFY("modify", "修改"),
    // 打印
    PRINT("print", "打印"),
    // 废弃
    DISCARD("discard", "作废")
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
}
