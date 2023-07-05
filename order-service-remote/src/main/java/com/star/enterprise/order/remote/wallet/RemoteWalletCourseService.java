package com.star.enterprise.order.remote.wallet;

import com.star.enterprise.order.remote.BalanceConfiguration;
import com.star.enterprise.wallet.api.WalletCourseServiceProvider;
import com.star.enterprise.wallet.api.response.WalletCourseDetailRecord;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static io.github.resilience4j.bulkhead.annotation.Bulkhead.Type.SEMAPHORE;
import static java.math.BigDecimal.ZERO;

/**
 * @author xiaowenrou
 * @date 2023/3/1
 */
@FeignClient(value = "star-wallet-service", contextId = "star-wallet-service-course")
@LoadBalancerClient(value = "star-wallet-service-course",configuration = BalanceConfiguration.class)
public interface RemoteWalletCourseService extends WalletCourseServiceProvider {

    /**
     * 重定义方法，使用 CircuitBreaker 断路器，并配置熔断方法
     * Feign BaseContract中获取方法注解时使用method自带的方法，无法获取父类的注解，需要在子类重新定义Http方法
     * @param orderItemId
     * @return
     */
    @Override
    @CircuitBreaker(name = "remote-service-breaker", fallbackMethod = "walletCourseDetailFallback")
    @Bulkhead(name = "remote-service-bulkhead", type = SEMAPHORE)
    @GetMapping(value = "/rpc/enterprise/wallet/course/detail")
    WalletCourseDetailRecord walletCourseDetail(@RequestParam("orderItemId") String orderItemId);

    /**
     * 熔断方法
     * @param orderItemId
     * @return
     */
    default WalletCourseDetailRecord walletCourseDetailFallback(String orderItemId, Throwable throwable) {
        return new WalletCourseDetailRecord("", "", "", "", "", ZERO, ZERO, ZERO, ZERO, ZERO, ZERO);
    }

}
