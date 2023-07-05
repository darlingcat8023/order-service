package com.star.enterprise.order.http.charge.request;

import com.star.enterprise.order.charge.model.ChargeProperties;
import com.star.enterprise.order.charge.model.impl.DefaultChargeProperties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static com.star.enterprise.order.charge.constants.rule.BaseChargeRuleSupport.CAMPUS;

/**
 * @author xiaowenrou
 * @date 2022/9/20
 */
public record CampusSaveRequest(

        @NotBlank(message = "business id not valid")
        String chargeItemId,

        @NotNull(message = "campuses not valid")
        List<String> campuses

) {

        public Collection<ChargeProperties> toProperties() {
                Function<String, ChargeProperties> function = str -> DefaultChargeProperties.builder().property(CAMPUS.getProperty()).propertyDesc(CAMPUS.getPropertyDesc()).standardValue(str).build();
                return campuses.stream().map(function).toList();
        }

}
