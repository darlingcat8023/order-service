package com.star.enterprise.order.remote.system;

import com.star.enterprise.order.remote.system.response.InLockRangeRecord;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

import static com.star.enterprise.order.base.utils.DateTimeUtils.DATE_PATTERN;
import static io.github.resilience4j.bulkhead.annotation.Bulkhead.Type.SEMAPHORE;

/**
 * @author xiaowenrou
 * @date 2023/4/4
 */
@FeignClient(value = "remote-system-service", url = "${remote.system}")
public interface RemoteSystemService {

    /**
     * 检查锁定区间
     * @param campus
     * @param dateTime
     * @return true: 锁定 false: 未锁定
     */
    @CircuitBreaker(name = "remote-service-breaker", fallbackMethod = "checkLockRangeFallback")
    @Bulkhead(name = "remote-service-bulkhead", type = SEMAPHORE)
    @GetMapping(value = "/api/system-service/public/finance-department/lock")
    InLockRangeRecord checkLockRange(@RequestParam(value = "department_id") String campus, @RequestParam(value = "date_time") @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime dateTime);

    /**
     * 服务降级
     * @param campus
     * @param dateTime
     * @param throwable
     * @return
     */
    default InLockRangeRecord checkLockRangeFallback(String campus, LocalDateTime dateTime, Throwable throwable) {
        return new InLockRangeRecord(true);
    }

}
