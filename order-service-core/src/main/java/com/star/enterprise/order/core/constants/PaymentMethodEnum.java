package com.star.enterprise.order.core.constants;

import com.star.enterprise.order.base.EnumSerialize;
import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
@AllArgsConstructor
public enum PaymentMethodEnum implements EnumSerialize {

    /**
     * Value
     */
    WECHAT("wechat_pay", "微信-收款"),

    ALIPAY("ali_pay", "支付宝-收款"),

    POS("POS", "POS机收款"),

    THIRD_DELAY("third_delay", "第三方延迟收款"),

    TRANSFER("transfer", "转账-汇款"),

    ONLINE_BACKEND("online_backend", "网校后台"),

    YOUZAN("you_zan", "有赞平台"),

    CASH("cash", "现金-收款"),

    QRCODE("qr_code", "二维码-收款")

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

    public static PaymentMethodEnum of(String method) {
        return Arrays.stream(values()).filter(e -> e.value().equals(method)).findAny().orElse(null);
    }

}
