package com.star.enterprise.order.message.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

/**
 * @author xiaowenrou
 * @date 2023/1/31
 */
@Configuration(proxyBeanMethods = false)
public class KafkaConsumerConfiguration {

    /**
     * 构建 ConsumerFactory
     * @param properties
     * @param customizers
     * @return
     */
    @Bean
    public ConsumerFactory<String, Object> consumerFactoryCustomizer(KafkaProperties properties, ObjectProvider<DefaultKafkaConsumerFactoryCustomizer> customizers) {
        var consumerMap = properties.buildConsumerProperties();
        consumerMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        consumerMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
        consumerMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        var factory = new DefaultKafkaConsumerFactory<>(consumerMap, new StringDeserializer(), new JsonDeserializer<>().trustedPackages("*"));
        customizers.orderedStream().forEach((customizer) -> customizer.customize(factory));
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory(ConsumerFactory<String, Object> kafkaConsumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(kafkaConsumerFactory);
        factory.setConcurrency(4);
        var properties = factory.getContainerProperties();
        properties.setPollTimeout(3000);
        return factory;
    }

}
