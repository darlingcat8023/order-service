package com.star.test;

import com.star.enterprice.orderservice.OrderServiceApplication;
import com.star.enterprise.order.http.order.request.TargetRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * @author xiaowenrou
 * @date 2022/9/13
 */
@SpringBootTest(classes = {OrderServiceApplication.class})
public class OrderServiceApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void test() {
        var target = new TargetRecord("123", "456", "123", null);

    }

}
