package com.star.test.service;

import com.star.enterprice.orderservice.OrderServiceApplication;
import com.star.enterprise.order.core.model.OrderSearchPredicate;
import com.star.enterprise.order.core.service.OrderSearchService;
import com.star.enterprise.order.core.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestOperations;

/**
 * @author xiaowenrou
 * @date 2022/10/31
 */
@SpringBootTest(classes = {OrderServiceApplication.class})
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderSearchService searchService;

    @Test
    public void testSummary() {
        var order = this.orderService.getOrderInfoSummaryObject("202212275427151578112");
        System.out.println(order);
    }

    @Test
    public void testSearch() {
        var condition = new OrderSearchPredicate();
        var ret = this.searchService.searchOrder(condition, null, PageRequest.of(0, 10));
        assert ret != null;
    }

    @Autowired
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Autowired
    private RestOperations restOperations;

    @Test
    public void testAsync() {


    }

}
