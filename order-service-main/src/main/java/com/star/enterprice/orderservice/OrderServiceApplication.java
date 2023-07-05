package com.star.enterprice.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Hooks;

/**
 * main
 * @author xiaowenrou
 * @date 2022/9/7
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.star")
public class OrderServiceApplication {

    public static void main(String[] args) {
        Hooks.onOperatorDebug();
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
