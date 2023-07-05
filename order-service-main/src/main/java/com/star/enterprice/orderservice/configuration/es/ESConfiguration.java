package com.star.enterprice.orderservice.configuration.es;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/10/28
 */
@AllArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableElasticsearchRepositories(basePackages = {"**.es"})
public class ESConfiguration {

    /**
     * 添加ES自定义转换器
     * @return
     */
    @Bean
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(List.of(new LongToLocalDateTimeConverter()));
    }

    @ReadingConverter
    public static class LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {

        @Override
        public LocalDateTime convert(@NonNull Long source) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(source), ZoneId.systemDefault());
        }

    }

}
