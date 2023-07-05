package com.star.enterprise.order.http.order.request;

import com.star.enterprise.order.core.model.PaymentInfo;
import com.star.enterprise.order.http.order.Verify;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public record OrderPaymentRecord(

        @NotBlank(message = "payment method not valid", groups = {Verify.OrderCreateVerify.class})
        String method,

        @NotNull(message = "payment amount not valid", groups = {Verify.OrderCreateVerify.class})
        BigDecimal paymentAmount,

        String paymentAccount

) implements PaymentInfo {


        @Override
        public String getOrderId() {
                return null;
        }

        @Override
        public String getPaymentMethod() {
                return this.method;
        }

        @Override
        public String getPaymentAccount() {
                return this.paymentAccount;
        }

        @Override
        public BigDecimal getPaymentAmount() {
                return this.paymentAmount;
        }
}
