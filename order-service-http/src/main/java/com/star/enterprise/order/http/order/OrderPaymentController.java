package com.star.enterprise.order.http.order;

import com.star.enterprise.order.core.data.jpa.entity.QOrderPaymentInfoEntity;
import com.star.enterprise.order.core.service.OrderPaymentService;
import com.star.enterprise.order.core.service.OrderDictionaryService;
import com.star.enterprise.order.http.order.response.OrderPaymentAggResponse;
import com.star.enterprise.order.http.order.response.OrderPaymentInfoResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/enterprise/order/payment")
public class OrderPaymentController {

    private final OrderPaymentService paymentService;

    /**
     * 支付方式枚举
     * @param paymentDictionaryService
     * @return
     */
    @GetMapping(value = "/dic/paymentMethod")
    public Map<?, ?> paymentMethodMap(@Value(value = "#{orderDictionaryService}") OrderDictionaryService paymentDictionaryService) {
        return paymentDictionaryService.buildPaymentMethodDictionary();
    }

    /**
     * 获取订单支付信息
     * @param orderId
     */
    @GetMapping(value = "/opt/list")
    public List<OrderPaymentInfoResponse> listPayment(@RequestParam(value = "orderId") String orderId) {
        return this.paymentService.getPayments(orderId).stream().map(item -> new OrderPaymentInfoResponse(item.getPaymentMethod(), item.getPaymentAccount(), item.getPaymentAmount())).toList();
    }

    /**
     * 支付方式汇总
     * @return
     */
    @GetMapping(value = "/opt/agg")
    public List<OrderPaymentAggResponse> paymentAgg() {
        return this.paymentService.paymentAggregation(tuple -> {
            var opi = QOrderPaymentInfoEntity.orderPaymentInfoEntity;
            return new OrderPaymentAggResponse(tuple.get(opi.paymentMethod), tuple.get(opi.paymentAmount.sum()));
        });
    }

}
