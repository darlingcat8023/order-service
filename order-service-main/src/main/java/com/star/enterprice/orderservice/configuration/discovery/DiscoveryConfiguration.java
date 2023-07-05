package com.star.enterprice.orderservice.configuration.discovery;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

/**
 * 启用服务发现
 * @author xiaowenrou
 * @date 2022/9/7
 */
@EnableDiscoveryClient
@Configuration(proxyBeanMethods = false)
public class DiscoveryConfiguration {

}
