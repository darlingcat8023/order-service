package com.star.enterprise.order.http.refund.request;

import com.star.enterprise.order.http.refund.VerifyGroup;
import com.star.enterprise.order.refund.model.RefundExtendInfo;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
@GroupSequenceProvider(value = RefundExtendGroupSequenceProvider.class)
public record OrderRefundExtendRecord(

        @NotBlank(message = "illegal refund method")
        String refundMethod,

        @NotBlank(message = "illegal bank account", groups = VerifyGroup.ValidWithCard.class)
        String bankAccount,

        String bankId,

        @NotBlank(message = "bank name can not be empty", groups = VerifyGroup.ValidWithCard.class)
        String bankName,

        @CreditCardNumber(message = "illegal card number", groups = VerifyGroup.ValidWithCard.class)
        String cardNumber,

        String remark,

        String refundReason,

        List<String> files

) implements RefundExtendInfo { }
