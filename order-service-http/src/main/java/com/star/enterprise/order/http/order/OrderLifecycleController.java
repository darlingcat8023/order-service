package com.star.enterprise.order.http.order;

import com.star.enterprise.order.base.Rest;
import com.star.enterprise.order.charge.constants.ChargeCategoryEnum;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.holder.AccumulateHolder;
import com.star.enterprise.order.core.service.OrderCalculateService;
import com.star.enterprise.order.core.service.OrderLifecycleService;
import com.star.enterprise.order.http.advice.security.SecurityAudit;
import com.star.enterprise.order.http.order.request.OrderFrontEndCollectRequest;
import com.star.enterprise.order.http.order.request.OrderModifyRequest;
import com.star.enterprise.order.http.order.request.PreCalculateOrderRequest;
import com.star.enterprise.order.http.order.response.*;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author xiaowenrou
 * @date 2022/11/1
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/enterprise/order")
public class OrderLifecycleController {

    private final OrderCalculateService calculateService;

    private final OrderLifecycleService lifeCycleService;

    /**
     * 获取前置计算结果
     * @param request
     * @return
     */
    @RateLimiter(name = "order-calculate")
    @PostMapping(value = "/pre/calculate")
    public PreCalculateResponse preOrderCalculate(@Validated @RequestBody PreCalculateOrderRequest request) {
        BiFunction<OrderFeeDetail, AccumulateHolder, PreCalculateResponse> function = (detail, holder) -> {
            var list = new ArrayList<PreOrderItemResponseRecord>();
            detail.items().forEach(x -> {
                var operator = x.operator();
                var plan = x.discountPlan();
                var discountPlan = new DiscountPlanResponseRecord(plan.discountPlanId(), plan.context().getDiscountPlanName(), plan.context().getDiscountPlanRate(), plan.apportion());
                var record = new PreOrderItemResponseRecord(x.businessType(), x.businessId(), x.specId(), x.extendInfo(), x.number(), operator.getOriginalSinglePrice(), operator.getOriginalTotalPrice(), discountPlan, operator.getAfterDiscountSinglePrice(), operator.getAfterDiscountTotalPrice(), x.additional(), x.webViewToast(), x.context().getSpecRecord());
                list.add(record);
            });
            return new PreCalculateResponse(request.webViewToast(), list);
        };
        return this.calculateService.preOrderCalculate(request.targetInfo(), request, function);
    }

    /**
     * 获取前置计算结果
     * @param request
     * @return
     */
    @RateLimiter(name = "order-calculate")
    @PostMapping(value = "/calculate")
    public OrderCalculateResponse orderCalculate(@Validated @RequestBody PreCalculateOrderRequest request) {
        BiFunction<OrderFeeDetail, AccumulateHolder, OrderCalculateResponse> function = (detail, acc) -> {
            var map = Arrays.stream(ChargeCategoryEnum.values()).collect(Collectors.toMap(ChargeCategoryEnum::value, ChargeCategoryEnum::desc));
            var wallet = detail.discount().walletDiscount();
            var coupons = detail.discount().couponDiscount().stream().map(coupon -> new CouponDiscountRecord(coupon.templateId(), coupon.context().getTemplateName(), coupon.couponCode(), coupon.order())).toList();
            var discountRecord = new DiscountInfoResponseRecord(detail.discount().useDirectDiscount(), coupons, new WalletDiscountRecord(wallet.useWallet(), wallet.context().getCurrentBalance()));
            var orderFee = new OrderFeeResponseRecord(acc.getUseCoupons(), acc.getOrderTotalPrice(), acc.getOrderAfterDiscountTotalPrice(), acc.getOrderDueCollectPrice());
            var records = new ArrayList<OrderItemResponseRecord>();
            detail.items().forEach(item -> {
                var opt = item.operator();
                var ctx = item.context();
                var plan = item.discountPlan();
                var discountPlan = new DiscountPlanResponseRecord(plan.discountPlanId(), plan.context().getDiscountPlanName(), plan.context().getDiscountPlanRate(), plan.apportion());
                var cmr = opt.getCalculatedChargeCategory().stream().map(res -> new ChargeMatchResultResponseRecord(res.chargeItemId(), res.chargeCategory(), map.get(res.chargeCategory()))).toList();
                var fd = new OrderItemFeeResponseRecord(opt.getOriginalSinglePrice(), opt.getOriginalTotalPrice(), opt.getAfterDiscountSinglePrice(), opt.getAfterDiscountTotalPrice(), discountPlan, opt.getUseDirect(), opt.getUseWallet(), opt.getDueCollectPrice(), cmr);
                var rec = new OrderItemResponseRecord(item.businessType(), item.businessId(), ctx.getProductName(), item.specId(), item.extendInfo(), item.number(), item.additional(), item.webViewToast(), fd, ctx.getSpecRecord());
                records.add(rec);
            });
            return new OrderCalculateResponse(request.webViewToast(), discountRecord, records, orderFee);
        };
        return this.calculateService.orderCalculate(request.targetInfo(), request, function);
    }

    /**
     * 保存订单
     * @param request
     * @return
     */
    @SecurityAudit
    @RateLimiter(name = "order-calculate")
    @PostMapping(value = "/save/frontend")
    public Rest<String> frontEndSave(@Validated(value = {Verify.OrderSaveVerify.class}) @RequestBody OrderFrontEndCollectRequest request) {
        var ret = this.lifeCycleService.saveOrderFromFrontend(new AtomicReference<>(request.orderId()), request.payload().targetInfo(), request);
        return Rest.just(ret);
    }

    /**
     * 结算
     * @param request
     * @return
     */
    @SecurityAudit
    @PostMapping(value = "/create/frontend")
    public Rest<String> frontEndCreate(@Validated(value = {Verify.OrderCreateVerify.class}) @RequestBody OrderFrontEndCollectRequest request) {
        var ret = this.lifeCycleService.createOrderFromFrontend(new AtomicReference<>(request.orderId()), request.payload().targetInfo(), request);
        return Rest.just(ret);
    }

    /**
     * 修改已经支付的订单
     * @param orderId
     *
     */
    @SecurityAudit
    @PostMapping(value = "/modify")
    public void modifyPaidOrder(@RequestParam(value = "orderId") String orderId, @Validated(value = {Verify.OrderModifyVerify.class}) @RequestBody OrderModifyRequest request) {
        this.lifeCycleService.modifyExistOrder(orderId, request, new HashMap<>(request.itemChargeCategories()));
    }

    /**
     * 取消订单
     * @param orderId
     */
    @SecurityAudit
    @PostMapping(value = "/cancel")
    public void frontEndCancel(@RequestParam(value = "orderId") String orderId) {
        this.lifeCycleService.cancelOrder(new AtomicReference<>(orderId));
    }

    /**
     * 删除订单
     * @param orderId
     */
    @SecurityAudit
    @PostMapping(value = "/delete")
    public void frontEndDelete(@RequestParam(value = "orderId") String orderId) {
        this.lifeCycleService.deleteOrder(new AtomicReference<>(orderId));
    }

}
