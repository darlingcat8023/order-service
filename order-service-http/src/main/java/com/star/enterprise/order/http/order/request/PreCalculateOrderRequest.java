package com.star.enterprise.order.http.order.request;

import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.OrderItemFeeDetail;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.star.enterprise.order.core.constants.OrderSourceEnum.FRONT_END;

/**
 * @author xiaowenrou
 * @date 2022/9/23
 */
public record PreCalculateOrderRequest(

        @Valid
        @NotNull(message = "target info not valid")
        TargetRecord targetInfo,

        LocalDateTime businessDate,

        String orderSource,

        @Valid
        DiscountInfoRecord discount,

        @Valid
        @NotNull(message = "item detail not valid")
        @Size(min = 1, message = "item detail not valid")
        List<OrderItemRecord> itemDetails,

        String webViewToast

) implements OrderFeeDetail {

        public PreCalculateOrderRequest(TargetRecord targetInfo, LocalDateTime businessDate, String orderSource, DiscountInfoRecord discount, List<OrderItemRecord> itemDetails, String webViewToast) {
                this.targetInfo = targetInfo;
                this.businessDate = Objects.requireNonNullElseGet(businessDate, LocalDateTime::now);
                this.orderSource = Objects.requireNonNullElseGet(orderSource, FRONT_END::value);
                this.discount = discount;
                this.itemDetails = itemDetails;
                this.webViewToast = webViewToast;
        }

        @Override
        public List<OrderItemFeeDetail> items() {
                return List.copyOf(this.itemDetails);
        }

}
