package com.star.enterprise.order.charge.service;

import com.star.enterprise.order.base.utils.EnumSerializeUtils;
import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.constants.ChargeCategoryEnum;
import com.star.enterprise.order.charge.constants.PriceAttributeEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2022/9/19
 */
@Service
@AllArgsConstructor
public class ChargeDictionaryService {

    public Map<String, String> buildChargeCategoryDictionary() {
        return EnumSerializeUtils.toMap(ChargeCategoryEnum.values());
    }

    public Map<String, String> buildFeeAttributeDictionary() {
        return EnumSerializeUtils.toMap(PriceAttributeEnum.values());
    }

    public Map<String, String> buildBusinessTypeDictionary() {
        return EnumSerializeUtils.toMap(BusinessTypeEnum.values());
    }

}
