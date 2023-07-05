package com.star.enterprise.order.http.order.request;

import com.star.enterprise.order.core.model.OrderCollectInfo;
import com.star.enterprise.order.core.model.PaymentInfo;
import com.star.enterprise.order.http.order.Verify.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public record OrderFrontEndCollectRequest(

        String orderId,

        @Valid
        @NotNull(message = "payload not valid", groups = {OrderSaveVerify.class, OrderCreateVerify.class})
        PreCalculateOrderRequest payload,

        AdditionalInformationRecord invoice,

        @Valid
        @NotNull(message = "payments not valid", groups = {OrderCreateVerify.class})
        List<OrderPaymentRecord> channels


) implements OrderCollectInfo {

        @Override
        public List<PaymentInfo> payments() {
                return List.copyOf(this.channels);
        }

}
