package com.star.enterprise.order.remote;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * 断路器配置，同时使用时执行顺序
 * Retry -> CircuitBreaker -> RateLimiter -> TimeLimiter -> Bulkhead
 * @author xiaowenrou
 * @date 2022/11/8
 */
@Configuration(proxyBeanMethods = false)
public class ResilienceConfiguration {

    /**
     * 远程服务断路器
     * @return
     */
    @Bean
    public CircuitBreaker remoteServiceBreaker(CircuitBreakerRegistry registry) {
        var config = CircuitBreakerConfig.custom()
                // 失败率大于 30% 时开始触发状态变更 close -> open, half_open -> open
                .failureRateThreshold(30)
                // 从 open -> half_open 的最大等待时间
                .waitDurationInOpenState(Duration.ofSeconds(30))
                // 断路器将在 half_open 状态无限等待，直到所有允许的调用都完成
                .maxWaitDurationInHalfOpenState(Duration.ofSeconds(30))
                // half_open 时允许的最大调用数量
                .permittedNumberOfCallsInHalfOpenState(20)
                // 自动从 half_open -> open, 如果设置 false 需要等待一个请求来触发
                .automaticTransitionFromOpenToHalfOpenEnabled(true);
        return registry.circuitBreaker("remote-service-breaker", config.build());
    }


    /**
     * 使用信号量进行故障隔离
     * @return
     */
    @Bean
    public Bulkhead remoteServiceBulkhead(BulkheadRegistry registry) {
        // 最大并发100， 最多等待3s，先等待请求的先执行
        var config = BulkheadConfig.custom().maxConcurrentCalls(100).maxWaitDuration(Duration.ofSeconds(3))
                .fairCallHandlingStrategyEnabled(true);
        return registry.bulkhead("remote-service-bulkhead", config.build());
    }

    /**
     * 故障重试
     * @return
     */
    @Bean
    public Retry remoteServiceRetry(RetryRegistry registry) {
        // 1s后重试
        var config = RetryConfig.custom().maxAttempts(2).waitDuration(Duration.ofSeconds(1));
        return registry.retry("remote-service-retry", config.build());
    }

}
