package com.star.enterprise.order.http.order.request;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.star.enterprise.order.core.calculator.OrderItemContext;
import com.star.enterprise.order.core.calculator.OrderItemFeeDetail;
import com.star.enterprise.order.core.calculator.OrderItemFeeOperator;
import com.star.enterprise.order.core.model.impl.DefaultFeeOperator;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author xiaowenrou
 * @date 2022/9/27
 */
public record OrderItemRecord(

        @NotBlank(message = "business type not valid")
        String businessType,

        @NotBlank(message = "business id not valid")
        String businessId,

        String specId,

        Map<String, Object> extendInfo,

        @NotNull(message = "number not valid")
        Integer number,

        int apportion,

        @Valid
        @NotNull
        DiscountPlanRecord discountPlan,

        boolean additional,

        String parentBusinessId,

        String callbackAddress,

        ChargeMatchResultRecord chargeCategory,

        String webViewToast,

        // 用来计算费用项的结构
        @JsonIgnore
        OrderItemFeeOperator operator,

        @JsonIgnore
        OrderItemContext context

) implements OrderItemFeeDetail {

    /**
     * record类需要有全参构造函数
     * @param businessType
     * @param businessId
     * @param specId
     * @param number
     * @param apportion
     * @param discountPlan
     * @param additional
     * @param chargeCategory
     * @param webViewToast
     * @param operator
     * @param context
     */
    public OrderItemRecord(String businessType, String businessId, String specId, Map<String, Object> extendInfo, Integer number, int apportion, DiscountPlanRecord discountPlan, boolean additional, String parentBusinessId, String callbackAddress, ChargeMatchResultRecord chargeCategory, String webViewToast, OrderItemFeeOperator operator, OrderItemContext context) {
        this.businessType = businessType;
        this.businessId = businessId;
        this.specId = specId;
        this.extendInfo = Objects.requireNonNullElseGet(extendInfo, HashMap::new);
        this.number = number;
        this.apportion = apportion;
        this.additional = additional;
        this.discountPlan = discountPlan;
        this.parentBusinessId = parentBusinessId;
        this.callbackAddress = callbackAddress;
        this.chargeCategory = chargeCategory;
        this.webViewToast = webViewToast;
        this.operator = new DefaultFeeOperator();
        this.context = new OrderItemContext();
    }

    /**
     * 未映射的属性都添加到 extendInfo
     * @param key
     * @param value
     */
    @JsonAnySetter
    public void add(String key, Object value) {
        this.extendInfo.put(key, value);
    }

}
