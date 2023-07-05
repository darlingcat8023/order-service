package com.star.enterprise.order.refund.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.core.model.OrderBusinessInfo;
import com.star.enterprise.order.refund.handler.business.BusinessTypeRefundHandler;
import lombok.Data;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
@Data
public class RefundOrderItemContext implements OrderBusinessInfo {

    private OrderItemDelegate delegate;

    private BusinessTypeRefundHandler handler;

    private String refundOrderItemId;

    @Override
    public String businessType() {
        return this.delegate.getBusinessType();
    }

    @Override
    public String businessId() {
        return this.delegate.getBusinessId();
    }

    @Override
    public String specId() {
        return this.delegate.getSpecId();
    }

    @Override
    public Map<String, Object> extendInfo() {
        return Jackson.read(this.delegate.getExtendInfo(), new TypeReference<>() {
        });
    }

    @Override
    public String webViewToast() {
        return this.delegate.getWebViewToast();
    }

}
