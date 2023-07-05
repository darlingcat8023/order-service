package com.star.test.service;

import com.star.enterprice.orderservice.OrderServiceApplication;
import com.star.enterprise.order.receipt.model.ReceiptSearchPredicate;
import com.star.enterprise.order.receipt.service.ReceiptSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/12/22
 */
@SpringBootTest(classes = {OrderServiceApplication.class})
public class ReceiptServiceTest {

    @Autowired
    private ReceiptSearchService searchService;

    @Test
    public void testReceiptSearch() {
        var condition = new ReceiptSearchPredicate();
        condition.setTarget("8c331058-2eed-4f97-8036-fc8b2fd4ee63");
        condition.setReceiptType(List.of("order_paid", "refund_order"));
        var ret = this.searchService.searchReceipt(condition, null, PageRequest.of(0, 10));
        assert ret != null;
    }

}
