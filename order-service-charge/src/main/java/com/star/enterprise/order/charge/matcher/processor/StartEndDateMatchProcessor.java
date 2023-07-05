package com.star.enterprise.order.charge.matcher.processor;

import com.star.enterprise.order.charge.matcher.Business;
import com.star.enterprise.order.charge.matcher.PropertyMatchProcessor;
import com.star.enterprise.order.charge.model.ChargeProperties;
import com.star.enterprise.order.charge.model.TargetUser;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import static com.star.enterprise.order.charge.constants.rule.BaseChargeRuleSupport.AVAILABLE_TIME;

/**
 * @author xiaowenrou
 * @date 2022/9/22
 */
@Component
public class StartEndDateMatchProcessor<B extends Business> implements PropertyMatchProcessor<B> {

    private final String prop = AVAILABLE_TIME.getProperty();

    @Override
    public boolean process(TargetUser target, B product, String chargeItemId, MultiValueMap<String, ChargeProperties> propertyMap) {
        if (!propertyMap.containsKey(this.prop)) {
            return true;
        }
        var date = propertyMap.get(this.prop).iterator().next();
        var current = System.currentTimeMillis();
        if (StringUtils.hasText(date.getMaxValue()) && current > Long.parseLong(date.getMaxValue())) {
            return false;
        }
        return !StringUtils.hasText(date.getMinValue()) || current >= Long.parseLong(date.getMinValue());
    }

}
