package com.star.enterprise.order.remote.student;

import com.star.enterprise.order.remote.student.request.RemoteStudentInfoRequest;
import com.star.enterprise.order.remote.student.response.RemoteStudentInfoResponse;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.star.enterprise.order.remote.student.RemoteStudentService.SERVICE_NAME;
import static io.github.resilience4j.bulkhead.annotation.Bulkhead.Type.SEMAPHORE;

/**
 * @author xiaowenrou
 * @date 2022/11/1
 */
@FeignClient(value = SERVICE_NAME, url = "${remote.student}")
public interface RemoteStudentService {

    String SERVICE_NAME = "remote-student-service";

    /**
     * 根据学生id获取学生信息
     * @param request
     * @return
     */
    @Retry(name = "remote-service-retry")
    @CircuitBreaker(name = "remote-service-breaker")
    @Bulkhead(name = "remote-service-bulkhead", type = SEMAPHORE)
    @Cacheable(value = "remote_student_info_cache", key = "#request.studentId()", cacheManager = "httpCacheManager")
    @PostMapping(value = "/inner/student-service/student/info")
    RemoteStudentInfoResponse getUserInfoByTargetId(@RequestBody RemoteStudentInfoRequest request);

}
