package com.star.test.service;

import com.star.enterprice.orderservice.OrderServiceApplication;
import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.core.service.OrderService;
import com.star.enterprise.order.http.order.request.TargetRecord;
import com.star.enterprise.order.http.refund.response.OrderRefundItemRecord;
import com.star.enterprise.order.receipt.data.jpa.ReceiptOrderPaidRepository;
import com.star.enterprise.order.refund.model.OrderItemDelegate;
import com.star.enterprise.order.refund.processor.DelegatingPredicateProcessor;
import com.star.enterprise.order.refund.service.OrderRefundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author xiaowenrou
 * @date 2023/3/2
 */
@SpringBootTest(classes = {OrderServiceApplication.class})
public class RefundServiceTest {

    @Autowired
    private OrderRefundService refundService;

    @Autowired
    private ApplicationContext context;

    @Test
    public void testRefund() {
        var target = new TargetRecord("8c331058-2eed-4f97-8036-fc8b2fd4ee63", "45f05ec8-3c38-423b-8106-d1bb21addefc", null, null);
        var refundList = this.refundService.findAvailableRefundOrder(target, true, true);
        var ret = new ArrayList<OrderRefundItemRecord>();
        refundList.forEach(refund -> {
            refund.getRefundItems().forEach(item -> {
                var object = refund.getObject();
                var fee = item.getItemFee();
                var balance = item.getNumberLeft().multiply(fee.getDueCollectSinglePrice());
                var record = new OrderRefundItemRecord("", item.getOrderId(), object.getAdditional().getInvoiceNo(), object.getCompletedDate(),
                        item.getOrderItemId(), item.getBusinessId(), item.getBusinessType(), item.getProductName(), new BigDecimal(item.getNumber()), new BigDecimal(item.getApportion()),
                        item.getNumberLeft(), item.getApportionLeft(), balance, fee.getOriginSinglePrice(), fee.getDueCollectSinglePrice(), item.getCurrentStandardPrice(), item.getDelegate());
                ret.add(record);
            });
        });
        System.out.println(ret.size());
    }

    @Autowired
    private ReceiptOrderPaidRepository repository;

    @Autowired
    private OrderService orderService;

    @Test
    public void testPredicate() {
        var receipt = repository.findByReceiptNo("1080028923347030016");
        var order = orderService.getOrderInfoSummaryObject(receipt.get().getOrderId());
        order.getItems().forEach(item -> {
            var predicate = DelegatingPredicateProcessor.build(this.context, BusinessTypeEnum.of(item.getBusinessType()));
            var bool = predicate.predicate(null, new OrderItemDelegate(item), order);
            System.out.println(bool);
        });
        System.out.println(1111);
    }

}
