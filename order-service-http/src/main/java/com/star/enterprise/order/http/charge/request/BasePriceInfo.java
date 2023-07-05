package com.star.enterprise.order.http.charge.request;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.charge.constants.PriceAttributeEnum;
import com.star.enterprise.order.charge.model.ChargeProperties;
import com.star.enterprise.order.charge.model.PriceInfo;
import com.star.enterprise.order.charge.model.impl.DefaultChargeProperties;

import javax.validation.constraints.NotBlank;

import static com.star.enterprise.order.base.exception.RestCode.ARGUMENT_NOT_VALID;
import static com.star.enterprise.order.charge.constants.rule.BaseChargeRuleSupport.PRICE_ATTR;

/**
 * @author xiaowenrou
 * @date 2022/9/19
 */
public record BasePriceInfo(

        @NotBlank(message = "price attribute not valid")
        String attribute,

        String standard,

        String min,

        String max

) implements PriceInfo {

    @Override
    public PriceAttributeEnum attributeEnum() {
        var e= PriceAttributeEnum.of(this.attribute);
        if (e == null) {
            throw new BusinessWarnException(ARGUMENT_NOT_VALID, "error.charge.noAttribute");
        }
        return e;
    }

    @Override
    public ChargeProperties convertChargeProperties() {
        var builder = DefaultChargeProperties.builder().property(PRICE_ATTR.getProperty()).propertyDesc(PRICE_ATTR.getPropertyDesc()).attribute(this.attribute);
        return builder.minValue(this.min).maxValue(this.max).build();
    }

}
