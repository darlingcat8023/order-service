package com.star.enterprise.order.http.refund;

import com.star.enterprise.order.base.Rest;
import com.star.enterprise.order.http.order.response.WalletDiscountRecord;
import com.star.enterprise.order.http.refund.request.OrderRefundCalculateRequest;
import com.star.enterprise.order.http.refund.request.OrderRefundCreateRequest;
import com.star.enterprise.order.http.refund.response.DelegatingOrderRefundItemRecord;
import com.star.enterprise.order.http.refund.response.OrderRefundCalculateResponse;
import com.star.enterprise.order.http.refund.response.OrderRefundFeeRecord;
import com.star.enterprise.order.http.refund.response.OrderRefundItemRecord;
import com.star.enterprise.order.refund.calculator.RefundAccumulateHolder;
import com.star.enterprise.order.refund.model.OrderRefundInfo;
import com.star.enterprise.order.refund.service.OrderRefundCalculateService;
import com.star.enterprise.order.refund.service.OrderRefundLifecycleService;
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
@RequestMapping(value = "/api/enterprise/order/refund")
@AllArgsConstructor
public class OrderRefundLifecycleController {

    private final OrderRefundCalculateService calculateService;

    private final OrderRefundLifecycleService lifecycleService;

    /**
     * 退费计算接口
     * @param request
     * @return
     */
    @PostMapping(value = "/calculate")
    public OrderRefundCalculateResponse calculate(@Validated @RequestBody OrderRefundCalculateRequest request) {
        BiFunction<OrderRefundInfo, RefundAccumulateHolder, OrderRefundCalculateResponse> biFunction = (info, holder) -> {
            var list = info.items().stream().map(item -> {
                var context = item.context().getDelegate();
                var fee = context.getItemFee();
                var opt = item.operator();
                var delegate = new OrderRefundItemRecord(item.receiptNo(), item.orderId(), item.invoiceNo(), item.purchasedDate(),
                        item.orderItemId(), item.businessId(), item.businessType(), context.getProductName(), new BigDecimal(context.getNumber()), new BigDecimal(context.getApportion()),
                        context.getNumberLeft(), context.getApportionLeft(), opt.getCurrentBalance(), fee.getOriginSinglePrice(), fee.getDueCollectSinglePrice(), context.getCurrentStandardPrice(), context.getDelegate());
                return new DelegatingOrderRefundItemRecord(delegate, item.refundNumber(), item.refundApportion(), opt.getRefundTotalPrice(), opt.getRefundOverPrice());
            }).toList();
            var record = new OrderRefundFeeRecord(holder.getDueRefundPrice(), holder.getFinalRefundPrice());
            var wallet = new WalletDiscountRecord(info.walletRefund().useWallet(), info.walletRefund().context().getCurrentBalance());
            return new OrderRefundCalculateResponse(list, info.additionalFee(), wallet, record);
        };
        return this.calculateService.calculate(request.target(), request, biFunction);
    }

    /**
     * 退费创建接口
     * @param request
     * @return
     */
    @PostMapping(value = "/opt/create")
    public Rest<String> createRefund(@Validated @RequestBody OrderRefundCreateRequest request) {
        var refundId = this.lifecycleService.createRefund(request.target(), request);
        return Rest.just(refundId);
    }


}
