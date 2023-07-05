package com.star.enterprice.orderservice.configuration.database;

import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.core.data.es.entity.SearchedOperator;
import com.star.enterprise.order.http.advice.security.WebSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

/**
 * JPA配置
 * @author xiaowenrou
 * @date 2022/9/7
 */
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EntityScan(basePackages = {"**.jpa"})
@EnableJpaRepositories(basePackages = {"**.jpa"})
@EnableTransactionManagement(proxyTargetClass = true)
@Configuration(proxyBeanMethods = false)
public class DataBaseConfiguration {

    /**
     * jpa审计实现
     * @return
     */
    @Bean
    public AuditorAware<String> auditorAware(ApplicationContext applicationContext) {
        return new StarAuditorAware(applicationContext);
    }

    @AllArgsConstructor
    static class StarAuditorAware implements AuditorAware<String> {

        private final ApplicationContext context;

        @Override
        public @NonNull Optional<String> getCurrentAuditor() {
            var opt = WebSecurityContext.getSecurity().map(emp -> new SearchedOperator(emp.employeeId(), emp.employeeName()))
                    .orElseGet(this::defaultOperator);
            return Optional.of(opt).map(Jackson::writeString);
        }

        protected SearchedOperator defaultOperator() {
            return SearchedOperator.defaultOperator();
        }

    }

}
