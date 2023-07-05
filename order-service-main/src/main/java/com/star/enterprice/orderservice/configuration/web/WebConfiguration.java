package com.star.enterprice.orderservice.configuration.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 启用自定义web配置
 * @author xiaowenrou
 * @date 2022/9/7
 */
@EnableWebMvc
@EnableSpringDataWebSupport
@AllArgsConstructor
@Configuration(proxyBeanMethods = false)
public class WebConfiguration implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
//        registry.addMapping("/**").allowedOriginPatterns("*")
//                .allowedMethods("GET", "POST", "OPTIONS", "DELETE", "PUT")
//                .allowedHeaders("*");
    }

    /**
     * 配置序列化逻辑
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (var converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jacksonConvertor) {
                jacksonConvertor.setObjectMapper(this.objectMapper);
            } else if (converter instanceof StringHttpMessageConverter stringConvertor) {
                // 移除框架自动添加的 string http message convertor
                stringConvertor.setDefaultCharset(UTF_8);
            }
        }
    }

    /**
     * 添加自定义参数解析
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> argumentResolvers) {

    }

}
