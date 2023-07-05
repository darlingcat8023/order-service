package com.star.enterprise.order.http.transfer;

import com.star.enterprise.order.base.Rest;
import com.star.enterprise.order.http.transfer.request.OrderTransferCalculateRequest;
import com.star.enterprise.order.http.transfer.request.OrderTransferCreateRequest;
import com.star.enterprise.order.http.transfer.response.DelegatingOrderTransferItemRecord;
import com.star.enterprise.order.http.transfer.response.OrderTransferCalculateResponse;
import com.star.enterprise.order.http.transfer.response.OrderTransferFeeRecord;
import com.star.enterprise.order.http.transfer.response.OrderTransferItemRecord;
import com.star.enterprise.order.transfer.calculator.TransferAccumulateHolder;
import com.star.enterprise.order.transfer.model.OrderTransferInfo;
import com.star.enterprise.order.transfer.service.OrderTransferCalculateService;
import com.star.enterprise.order.transfer.service.OrderTransferLifecycleService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.function.BiFunction;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
@RestController
@RequestMapping(value = "/api/enterprise/order/transfer")
@AllArgsConstructor
public class OrderTransferLifecycleController {

    private final OrderTransferCalculateService calculateService;

    private final OrderTransferLifecycleService lifecycleService;

    /**
     * 退费计算接口
     * @param request
     * @return
     */
    @PostMapping(value = "/calculate")
    public OrderTransferCalculateResponse calculate(@Validated @RequestBody OrderTransferCalculateRequest request) {
        BiFunction<OrderTransferInfo, TransferAccumulateHolder, OrderTransferCalculateResponse> biFunction = (info, holder) -> {
            var list = info.items().stream().map(item -> {
                var context = item.context().getDelegate();
                var fee = context.getItemFee();
                var opt = item.operator();
                var delegate = new OrderTransferItemRecord(item.receiptNo(), item.orderId(), item.invoiceNo(), item.purchasedDate(),
                        item.orderItemId(), item.businessId(), item.businessType(), context.getProductName(), new BigDecimal(context.getNumber()), new BigDecimal(context.getApportion()),
                        context.getNumberLeft(), context.getApportionLeft(), opt.getCurrentBalance(), fee.getOriginSinglePrice(), fee.getDueCollectSinglePrice(), context.getCurrentStandardPrice(), context.getDelegate());
                return new DelegatingOrderTransferItemRecord(delegate, item.transferNumber(), item.transferApportion(), opt.getTransferTotalPrice());
            }).toList();
            var record = new OrderTransferFeeRecord(holder.getDueTransferPrice(), holder.getFinalTransferPrice());
            return new OrderTransferCalculateResponse(list, info.additionalFee(), record);
        };
        return this.calculateService.calculate(request.target(), request, biFunction);
    }

    /**
     * 退费创建接口
     * @param request
     * @return
     */
    @PostMapping(value = "/opt/create")
    public Rest<String> createRefund(@Validated @RequestBody OrderTransferCreateRequest request) {
        var refundId = this.lifecycleService.createTransfer(request.target(), request);
        return Rest.just(refundId);
    }

}
