package com.star.enterprise.order.http;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author xiaowenrou
 * @date 2023/2/3
 */
@Configuration
public class HttpRateLimiterConfiguration {

    @Bean
    public RateLimiter calculateLimit(RateLimiterRegistry registry) {
        var config = RateLimiterConfig.custom().limitRefreshPeriod(Duration.ofSeconds(5)).limitForPeriod(10);
        return registry.rateLimiter("order-calculate", config.build());
    }

}
