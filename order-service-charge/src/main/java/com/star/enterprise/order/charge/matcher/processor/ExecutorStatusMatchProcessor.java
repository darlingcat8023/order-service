package com.star.enterprise.order.charge.matcher.processor;

import com.star.enterprise.order.charge.matcher.PropertyMatchProcessor;
import com.star.enterprise.order.charge.matcher.business.CourseChargeBusiness;
import com.star.enterprise.order.charge.model.ChargeProperties;
import com.star.enterprise.order.charge.model.TargetUser;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import static com.star.enterprise.order.charge.constants.rule.CourseChargeRuleSupport.EXECUTOR_STATUS;

/**
 * 处理学员状态
 * @author xiaowenrou
 * @date 2022/9/22
 */
@Component
public class ExecutorStatusMatchProcessor implements PropertyMatchProcessor<CourseChargeBusiness> {

    private final String prop = EXECUTOR_STATUS.getProperty();

    @Override
    public boolean process(TargetUser target, CourseChargeBusiness product, String chargeItemId, MultiValueMap<String, ChargeProperties> propertyMap) {
        if (propertyMap.containsKey(this.prop)) {
            return propertyMap.get(this.prop).stream().map(ChargeProperties::getStandardValue).anyMatch(target.executorStatus()::equals);
        }
        return true;
    }

}
