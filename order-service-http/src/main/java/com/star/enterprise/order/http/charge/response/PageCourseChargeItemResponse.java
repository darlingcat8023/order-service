package com.star.enterprise.order.http.charge.response;

import com.star.enterprise.order.base.serialize.LocalDateTimeValue;
import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.model.ChargeProperties;
import com.star.enterprise.order.charge.model.impl.DefaultChargeItemDetail;
import com.star.enterprise.order.http.charge.request.BasePriceInfo;
import com.star.enterprise.order.http.charge.request.BusinessSpec;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.star.enterprise.order.charge.constants.rule.BaseChargeRuleSupport.*;
import static com.star.enterprise.order.charge.constants.rule.CourseChargeRuleSupport.*;

/**
 * @author xiaowenrou
 * @date 2022/9/20
 */
public record PageCourseChargeItemResponse(

        Long id,

        String chargeItemId,

        String businessType,

        String businessId,

        List<BusinessSpec> businessSpecs,

        List<String> executorStatus,

        BasePriceInfo priceInfo,

        Integer limit,

        @LocalDateTimeValue
        Long startTime,

        @LocalDateTimeValue
        Long endTime,

        List<String> campuses,

        String category,

        String enable

) {

    /**
     * 从service的返回值构造响应
     * @param detail
     * @return
     */
    public static PageCourseChargeItemResponse buildResponse(DefaultChargeItemDetail detail) {
        var propMap = detail.getProperties().stream().collect(Collectors.groupingBy(ChargeProperties::getProperty));
        var specList = new ArrayList<BusinessSpec>();
        if (propMap.containsKey(COURSE_SPEC.getProperty())) {
            propMap.get(COURSE_SPEC.getProperty()).forEach(prop -> specList.add(Jackson.read(prop.getPenetrate(), BusinessSpec.class)));
        }
        var statusList = new ArrayList<String>();
        if (propMap.containsKey(EXECUTOR_STATUS.getProperty())) {
            propMap.get(EXECUTOR_STATUS.getProperty()).forEach(prop -> statusList.add(prop.getStandardValue()));
        }
        Integer limit = null;
        if (propMap.containsKey(LIMIT.getProperty())) {
            limit = Integer.valueOf(propMap.get(LIMIT.getProperty()).get(0).getStandardValue());
        }
        Long startTime = null, endTime = null;
        if (propMap.containsKey(AVAILABLE_TIME.getProperty())) {
            var p = propMap.get(AVAILABLE_TIME.getProperty()).get(0);
            if (StringUtils.hasText(p.getMinValue())) {
                startTime = Long.valueOf(p.getMinValue());
            }
            if (StringUtils.hasText(p.getMaxValue())) {
                endTime = Long.valueOf(p.getMaxValue());
            }
        }
        BasePriceInfo price = null;
        if (propMap.containsKey(PRICE_ATTR.getProperty())) {
            var p = propMap.get(PRICE_ATTR.getProperty()).get(0);
            price = new BasePriceInfo(p.getAttribute(), p.getStandardValue(), p.getMinValue(), p.getMaxValue());
        }
        var campus = new ArrayList<String>();
        if (propMap.containsKey(CAMPUS.getProperty())) {
            propMap.get(CAMPUS.getProperty()).stream().map(ChargeProperties::getStandardValue).forEach(campus::add);
        }
        return new PageCourseChargeItemResponse(detail.getId(), detail.getChargeItemId(), detail.getBusinessType(), detail.getBusinessId(), specList, statusList, price, limit, startTime, endTime, campus,detail.getCategory(), detail.getEnable());
    }

}
