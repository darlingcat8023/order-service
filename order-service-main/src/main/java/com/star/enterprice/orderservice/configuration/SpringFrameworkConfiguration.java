package com.star.enterprice.orderservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.star.enterprise.order.base.serialize.support.ScaledBigDecimalDeserializer;
import com.star.enterprise.order.base.serialize.support.ScaledBigDecimalSerializer;
import com.star.enterprise.order.base.utils.RecordJacksonAnnotationIntrospector;
import com.star.enterprise.order.base.utils.SnowflakeGenerator;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TimeZone;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.star.enterprise.order.base.utils.DateTimeUtils.*;
import static java.math.RoundingMode.HALF_UP;

/**
 * Spring框架配置
 * @author xiaowenrou
 * @date 2022/9/7
 */
@EnableConfigurationProperties
@EnableAsync(proxyTargetClass = true)
@EnableAspectJAutoProxy(exposeProxy = true)
@Configuration(proxyBeanMethods = false)
public class SpringFrameworkConfiguration {

    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {

        // 配置日期类型序列化和反序列化
        var timeModule = new JavaTimeModule();

        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));

        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));

        timeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(TIME_FORMATTER));
        timeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(TIME_FORMATTER));

        // 配置金额类型的序列化和反序列化
        var simpleModule = new SimpleModule();

        var scale = 2;
        var mode = HALF_UP;

        simpleModule.addSerializer(BigDecimal.class, new ScaledBigDecimalSerializer(scale, mode));
        simpleModule.addDeserializer(BigDecimal.class, new ScaledBigDecimalDeserializer(scale, mode));

        return builder.createXmlMapper(false).annotationIntrospector(new RecordJacksonAnnotationIntrospector())
                .modules(timeModule, simpleModule).timeZone(TimeZone.getTimeZone("GMT+8"))
                .failOnEmptyBeans(false).failOnUnknownProperties(false)
                .build();
    }

    /**
     * 配置参数校验为快速失败模式
     * 避免无谓计算，提升响应速度
     * @return
     */
    @Bean
    public Validator validator(){
        return Validation.byProvider(HibernateValidator.class).configure()
                .failFast(true).buildValidatorFactory().getValidator();
    }

    /**
     * Spring异步任务线程池(处理异步任务)
     * @return
     */
    @Bean(name = "asyncTaskExecutor")
    public ThreadPoolTaskExecutor asyncTaskExecutor(){
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("async-pool-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    /**
     * Spring异步任务线程池(发布异步事件)
     * @return
     */
    @Bean(name = "eventTaskExecutor")
    public ThreadPoolTaskExecutor eventTaskExecutor(){
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("event-pool-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    /**
     * 雪花生成器
     * @return
     */
    @Bean
    public SnowflakeGenerator idGenerator() {
        return new SnowflakeGenerator();
    }

    /**
     * RestTemplate
     * @return
     */
    @Bean
    public RestOperations restOperations() {
        var manager = new PoolingHttpClientConnectionManager(10L, TimeUnit.SECONDS);
        manager.setMaxTotal(100);
        manager.setDefaultMaxPerRoute(10);
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(HttpClients.createMinimal(manager)));
    }

}
