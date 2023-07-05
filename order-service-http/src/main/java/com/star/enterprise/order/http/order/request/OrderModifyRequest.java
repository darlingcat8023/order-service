package com.star.enterprise.order.http.order.request;

import com.star.enterprise.order.core.model.OrderExtendInfo;
import com.star.enterprise.order.core.model.PaymentInfo;
import com.star.enterprise.order.http.order.Verify;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2022/12/29
 */
public record OrderModifyRequest(

        AdditionalInformationRecord invoice,

        @Valid
        @NotNull(message = "payments not valid", groups = {Verify.OrderModifyVerify.class})
        List<OrderPaymentRecord> channels,

        Map<String, ChargeMatchResultRecord> itemChargeCategories

) implements OrderExtendInfo {

    @Override
    public List<PaymentInfo> payments() {
        return List.copyOf(this.channels);
    }

}
