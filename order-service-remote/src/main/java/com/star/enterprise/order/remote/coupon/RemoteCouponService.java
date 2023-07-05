package com.star.enterprise.order.remote.coupon;

import com.star.enterprise.order.remote.GolangFormatErrorDecoder;
import com.star.enterprise.order.remote.UseErrorDecoder;
import com.star.enterprise.order.remote.coupon.request.CouponCalculateRecord;
import com.star.enterprise.order.remote.coupon.request.CouponLockRecord;
import com.star.enterprise.order.remote.coupon.response.CalculatedItemRecord;
import com.star.enterprise.order.remote.coupon.response.LockRes;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static io.github.resilience4j.bulkhead.annotation.Bulkhead.Type.SEMAPHORE;

/**
 * @author xiaowenrou
 * @date 2023/3/20
 */
@FeignClient(value = "remote-coupon-service", url = "${remote.coupon}")
public interface RemoteCouponService {

    /**
     * 计算优惠券
     * @param targetId
     * @param campus
     * @param request
     * @return
     */
    @UseErrorDecoder(value = GolangFormatErrorDecoder.class)
    @CircuitBreaker(name = "remote-service-breaker")
    @Bulkhead(name = "remote-service-bulkhead", type = SEMAPHORE)
    @PostMapping(value = "/api/coupon-service/coupon-use/discount")
    Map<String, CalculatedItemRecord> calculateCouponDiscount(@RequestParam(value = "targetId") String targetId, @RequestParam(value = "campus") String campus, @RequestBody CouponCalculateRecord request);

    /**
     * 锁定优惠券
     * @param targetId
     * @param campus
     * @param record
     * @return
     */
    @CircuitBreaker(name = "remote-service-breaker", fallbackMethod = "lockCouponsFallback")
    @Bulkhead(name = "remote-service-bulkhead", type = SEMAPHORE)
    @PostMapping(value = "/api/coupon-service/coupon-use/coupon-lock")
    LockRes lockCoupons(@RequestParam(value = "targetId") String targetId, @RequestParam(value = "campus") String campus, @RequestBody CouponLockRecord record);

    /**
     * 服务降级，直接失败
     * @param targetId
     * @param campus
     * @param record
     * @param throwable
     * @return
     */
    default LockRes lockCouponsFallback(String targetId, String campus, CouponLockRecord record, Throwable throwable) {
        return new LockRes(false);
    }
}
