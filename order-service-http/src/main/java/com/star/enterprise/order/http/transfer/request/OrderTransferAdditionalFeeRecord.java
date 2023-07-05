package com.star.enterprise.order.http.transfer.request;

import com.star.enterprise.order.transfer.model.TransferAdditionalFee;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public record OrderTransferAdditionalFeeRecord(

        @NotNull(message = "confirmFee can not be empty")
        BigDecimal confirmFee,

        @NotNull(message = "manageFee can not be empty")
        BigDecimal manageFee,

        @NotNull(message = "handlingFee can not be empty")
        BigDecimal handlingFee,

        @NotNull(message = "cardFee can not be empty")
        BigDecimal cardFee,

        @NotNull(message = "offlineFee can not be empty")
        BigDecimal offlineFee,

        @NotNull(message = "dataFee can not be empty")
        BigDecimal dataFee

) implements TransferAdditionalFee {
}
