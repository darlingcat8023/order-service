package com.star.test.service;

import com.star.enterprice.orderservice.OrderServiceApplication;
import com.star.enterprise.order.core.service.OrderPaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

/**
 * @author xiaowenrou
 * @date 2022/12/20
 */
@SpringBootTest(classes = {OrderServiceApplication.class})
public class OrderPaymentServiceTest {

    @Autowired
    private OrderPaymentService paymentService;

    @Test
    public void testAgg() {
        var tuple = this.paymentService.paymentAggregation(Function.identity());
        System.out.println(1111);
    }

}
