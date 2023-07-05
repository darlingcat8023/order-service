package com.star.enterprise.order.http.order;

import com.star.enterprise.order.core.model.trans.OrderAdditionalSummaryTransObject;
import com.star.enterprise.order.core.service.OrderAdditionalService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaowenrou
 * @date 2022/10/27
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/enterprise/order/invoice")
public class OrderInvoiceController {

    private final OrderAdditionalService invoiceService;

    /**
     * 获取订单的发票信息
     * @param orderId
     * @return
     */
    @GetMapping(value = "/opt/list")
    public OrderAdditionalSummaryTransObject listPayment(@RequestParam(value = "orderId") String orderId) {
        return this.invoiceService.getOrderInvoiceInfo(orderId);
    }


}
