package com.star.enterprise.order.remote.wallet;

import com.star.enterprise.order.remote.BalanceConfiguration;
import com.star.enterprise.wallet.api.WalletBalanceServiceProvider;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

import static io.github.resilience4j.bulkhead.annotation.Bulkhead.Type.SEMAPHORE;

/**
 * @author xiaowenrou
 * @date 2023/3/10
 */
@FeignClient(value = "star-wallet-service", contextId = "star-wallet-service-balance")
@LoadBalancerClient(value = "star-wallet-service-balance",configuration = BalanceConfiguration.class)
public interface RemoteWalletBalanceService extends WalletBalanceServiceProvider {


    @Retry(name = "remote-service-retry", fallbackMethod = "fetchBalanceFallback")
    @CircuitBreaker(name = "remote-service-breaker", fallbackMethod = "fetchBalanceFallback")
    @Bulkhead(name = "remote-service-bulkhead", type = SEMAPHORE)
    @Cacheable(value = "remote_wallet_balance_cache", key = "#targetId", cacheManager = "httpCacheManager")
    @GetMapping({"/rpc/enterprise/wallet/balance/fetch"})
    BigDecimal fetchBalance(@RequestParam("targetId") String targetId, @RequestParam("campus") String campus);

    /**
     * 降级方法
     * @param targetId
     * @param campus
     * @param throwable
     * @return
     */
    default BigDecimal fetchBalanceFallback(String targetId, String campus, Throwable throwable) {
        return BigDecimal.ZERO;
    }

}
