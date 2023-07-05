package com.star.enterprise.order.core.service;

import com.star.enterprise.order.base.utils.EnumSerializeUtils;
import com.star.enterprise.order.core.constants.OrderStatusEnum;
import com.star.enterprise.order.core.constants.PaymentMethodEnum;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
@Service
public class OrderDictionaryService {

    public Map<String, String> buildPaymentMethodDictionary() {

        return EnumSerializeUtils.toMap(PaymentMethodEnum.values());
    }

    public Map<String, String> buildOrderStatusDictionary() {
        return EnumSerializeUtils.toMap(OrderStatusEnum.values());
    }

}
