package com.star.enterprise.order.charge.constants;

import com.star.enterprise.order.base.EnumSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 费用类型配置
 * @author xiaowenrou
 * @date 2022/9/16
 */
@AllArgsConstructor
public enum ChargeCategoryEnum implements EnumSerialize {

    /**
     * value
     */
    NEW("new", "新增", true),
    RENEW("renew", "续费", true),
    TRAINING_NEW("training_new", "集训新增", true),
    EXPANSION("expansion", "扩科", true),
    UPGRADE("upgrade", "课程升级", true),
    GOODS("goods", "物品", true),
    COMPETITION("competition", "赛事", true),
    INTEREST_CLASS("interest_class", "兴趣班", true),
    DRAINAGE("drainage", "低价引流", true),
    COOPERATION("cooperation", "合作项目", true),
    TRANSFER("transfer", "转校转费", true),
    OTHER("other", "其他", true),
    DEPOSIT("deposit", "预存-订金", true),
    DEPOSIT_NEW("deposit_new", "预存-新增", true),
    DEPOSIT_RENEW("deposit_renew", "预存-续费", true),
    DEPOSIT_OTHER("deposit_other", "预存-其他", true),
    ;

    private final String value;

    private final String desc;

    @Getter
    private final boolean enable;

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public String desc() {
        return this.desc;
    }

}
