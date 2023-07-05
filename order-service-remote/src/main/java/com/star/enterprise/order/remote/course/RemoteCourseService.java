package com.star.enterprise.order.remote.course;

import com.star.enterprise.order.remote.course.response.RemoteArticleResponse;
import com.star.enterprise.order.remote.course.response.RemoteCoursePriceResponse;
import com.star.enterprise.order.remote.course.response.RemotePriceResponse;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static io.github.resilience4j.bulkhead.annotation.Bulkhead.Type.SEMAPHORE;

/**
 * @author xiaowenrou
 * @date 2022/9/23
 */
@FeignClient(value = "remote-course-service", url = "${remote.course}")
public interface RemoteCourseService {

    /**
     * 获取课程价格
     * @param courseId
     * @param specId
     * @param campusId
     * @return
     */
    @Retry(name = "remote-service-retry")
    @CircuitBreaker(name = "remote-service-breaker")
    @Bulkhead(name = "remote-service-bulkhead", type = SEMAPHORE)
    @Cacheable(value = "remote_course_info_cache", key = "#courseId.concat('@@').concat(#specId).concat('@@').concat(#campusId)", cacheManager = "httpCacheManager")
    @GetMapping(value = "/api/course-service/public/course-price/specification")
    RemoteCoursePriceResponse getCoursePriceById(@RequestParam(value = "course_id") String courseId, @RequestParam(value = "spe_id") String specId, @RequestParam(value = "sch_id") String campusId);

    /**
     * 获取物品单价
     * @param articleId
     * @param campusId
     * @return
     */
    @Retry(name = "remote-service-retry")
    @CircuitBreaker(name = "remote-service-breaker")
    @Bulkhead(name = "remote-service-bulkhead", type = SEMAPHORE)
    @Cacheable(value = "remote_article_info_cache", key = "#articleId.concat('@@').concat(#campusId)", cacheManager = "httpCacheManager")
    @GetMapping(value = "/api/course-service/public/article/info")
    RemoteArticleResponse getArticlePriceById(@RequestParam(value = "article_id") String articleId, @RequestParam(value = "sch_id") String campusId);

    /**
     * 获取赛事单价
     * @param matchId
     * @param campusId
     * @return
     */
    @Retry(name = "remote-service-retry")
    @CircuitBreaker(name = "remote-service-breaker")
    @Bulkhead(name = "remote-service-bulkhead", type = SEMAPHORE)
    @Cacheable(value = "remote_match_info_cache", key = "#matchId.concat('@@').concat(#campusId)", cacheManager = "httpCacheManager")
    @GetMapping(value = "/api/course-service/public/match/info")
    RemotePriceResponse getMatchPriceById(@RequestParam(value = "match_id") String matchId, @RequestParam(value = "sch_id") String campusId);


}
