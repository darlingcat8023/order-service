package com.star.enterprise.order.remote;

import feign.Logger;
import feign.Request;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author xiaowenrou
 * @date 2022/9/23
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(value = FeignClientsConfiguration.class)
@EnableFeignClients(basePackages = "**.remote")
public class OpenFeignConfiguration {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * 响应info级别日志
     * @return
     */
    @Bean
    public Logger feignLogger() {
        return new FeignInfoLevelLogger();
    }

    /**
     * 超时时间配置
     * @return
     */
    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(3, SECONDS,10, SECONDS,true);
    }

    /**
     * 远程服务异常处理
     * @return
     */
    @Bean
    public ErrorDecoder errorHandler() {
        return new DelegatingErrorDecoder(new DefaultErrorDecoder());
    }

}
