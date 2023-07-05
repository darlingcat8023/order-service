package com.star.enterprise.order.core.model.trans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.star.enterprise.order.core.data.jpa.entity.OrderItemInfoEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/10/26
 */
@Data
@Accessors(chain = true)
public class OrderItemSummaryTransObject {

    @JsonIgnore
    private String orderId;

    private String orderItemId;

    private String businessType;

    private String businessId;

    private String productName;

    private String specId;

    private String specName;

    @JsonRawValue
    private String extendInfo;

    private Integer number;

    private BigDecimal originPrice;

    private boolean additional;

    private Integer apportion;

    private String itemStatus;

    private String callbackAddress;

    private String webViewToast;

    private OrderItemFeeSummaryTransObject itemFee;

    public OrderItemSummaryTransObject(OrderItemInfoEntity entity, OrderItemFeeSummaryTransObject obj) {
        BeanUtils.copyProperties(entity, this);
        this.itemFee = obj;
        this.additional = entity.getAdditional().equals(1);
    }

}
