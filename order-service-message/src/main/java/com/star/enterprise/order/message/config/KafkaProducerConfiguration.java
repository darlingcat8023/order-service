package com.star.enterprise.order.message.config;

import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.support.serializer.ToStringSerializer;

/**
 * @author xiaowenrou
 * @date 2022/9/7
 */
@EnableKafka
@Configuration(proxyBeanMethods = false)
public class KafkaProducerConfiguration {

    /**
     * 自定义KafkaProducerFactory配置
     * @return
     */
    @Bean
    public DefaultKafkaProducerFactoryCustomizer producerFactoryCustomizer() {
        return factory -> {
            factory.setKeySerializerSupplier(ToStringSerializer::new);
            factory.setValueSerializerSupplier(JsonSerializer::new);
        };
    }

    /**
     * 使用KafkaOperations发送消息
     * @param kafkaProducerFactory
     * @return
     */
    @Bean
   public KafkaOperations<String, Object> kafkaOperations(ProducerFactory<String, Object> kafkaProducerFactory) {
        return new KafkaTemplate<>(kafkaProducerFactory);
   }

}
