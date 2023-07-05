package com.star.enterprise.order.remote.security;

import com.star.enterprise.order.remote.security.request.RemoteTokenVerifyRequest;
import com.star.enterprise.order.remote.security.response.RemoteTokenVerifyResponse;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static io.github.resilience4j.bulkhead.annotation.Bulkhead.Type.SEMAPHORE;

/**
 * 远程安全服务
 * @author xiaowenrou
 * @date 2023/1/3
 */
@FeignClient(value = "remote-security-service", url = "${remote.security}")
public interface RemoteSecurityService {

    /**
     * 解析token
     * @param request
     * @return
     */
    @Retry(name = "remote-service-retry", fallbackMethod = "verifyTokenFallback")
    @CircuitBreaker(name = "remote-service-breaker")
    @Bulkhead(name = "remote-service-bulkhead", type = SEMAPHORE)
    @Cacheable(value = "remote_token_info_cache", key = "#request.token()", cacheManager = "httpCacheManager")
    @PostMapping(value = "/api/public/organization-service/permission/parse-token")
    RemoteTokenVerifyResponse verifyToken(@RequestBody RemoteTokenVerifyRequest request);

    /**
     * 降级方法，token不需要强行解析
     * @param request
     * @param throwable
     * @return
     */
    default RemoteTokenVerifyResponse verifyTokenFallback(RemoteTokenVerifyRequest request, Throwable throwable) {
        return new RemoteTokenVerifyResponse("0", "system", null, List.of());
    }

}
