package com.star.enterprise.order.http.transfer.request;

import com.star.enterprise.order.http.order.request.TargetRecord;
import com.star.enterprise.order.transfer.model.OrderTransferInfo;
import com.star.enterprise.order.transfer.model.OrderTransferItemInfo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public record OrderTransferCalculateRequest(
        @Valid
        @NotNull(message = "refund info not valid")
        @Size(message = "illegal transfer item", min = 1)
        List<OrderTransferItemRecord> records,

        @Valid
        @NotNull(message = "additional fee not valid")
        OrderTransferAdditionalFeeRecord additionalFee,

        @Valid
        @NotNull(message = "target info not valid")
        TargetRecord target

) implements OrderTransferInfo {

        @Override
        public List<OrderTransferItemInfo> items() {
                return new ArrayList<>(this.records);
        }

}
