package com.star.enterprise.order.charge.matcher.processor;

import com.star.enterprise.order.charge.constants.PriceAttributeEnum;
import com.star.enterprise.order.charge.matcher.Business;
import com.star.enterprise.order.charge.matcher.PropertyMatchProcessor;
import com.star.enterprise.order.charge.model.ChargeProperties;
import com.star.enterprise.order.charge.model.TargetUser;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

import static com.star.enterprise.order.charge.constants.rule.BaseChargeRuleSupport.PRICE_ATTR;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
@Component
public class PriceAttributeMatchProcessor<B extends Business> implements PropertyMatchProcessor<B> {

    private final String prop = PRICE_ATTR.getProperty();

    @Override
    public boolean process(TargetUser target, B product, String chargeItemId, MultiValueMap<String, ChargeProperties> propertyMap) {
        if (propertyMap.containsKey(this.prop)) {
            var priceProperty = propertyMap.get(this.prop).iterator().next();
            var e = Objects.requireNonNull(PriceAttributeEnum.of(priceProperty.getAttribute()));
            if (PriceAttributeEnum.NONE.equals(e)) {
                return true;
            }
            var targetValue = e.targetValue(product.fee());
            if (StringUtils.hasText(priceProperty.getMinValue())) {
                if (new BigDecimal(priceProperty.getMinValue()).compareTo(targetValue) > 0) {
                    return false;
                }
            }
            if (StringUtils.hasText(priceProperty.getMaxValue())) {
                return new BigDecimal(priceProperty.getMaxValue()).compareTo(targetValue) >= 0;
            }
            return true;
        }
        return true;
    }

}
