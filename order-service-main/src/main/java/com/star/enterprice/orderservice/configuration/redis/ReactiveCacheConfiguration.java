package com.star.enterprice.orderservice.configuration.redis;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.time.Duration;

/**
 * @author xiaowenrou
 * @date 2022/9/9
 */
@EnableCaching(proxyTargetClass = true, order = Ordered.HIGHEST_PRECEDENCE)
@Configuration(proxyBeanMethods = false)
public class ReactiveCacheConfiguration {

    /**
     * http缓存
     * @return
     */
    @Bean
    public CacheManager httpCacheManager() {
        var caffeine = Caffeine.newBuilder().expireAfterWrite(Duration.ofSeconds(300))
                .initialCapacity(500).maximumSize(500);
        var manager = new CaffeineCacheManager();
        manager.setCaffeine(caffeine);
        return manager;
    }

}
