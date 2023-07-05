package com.star.enterprise.order.core.strategy;

import com.star.enterprise.order.base.utils.SnowflakeGenerator;
import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.constants.OrderSourceEnum;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author xiaowenrou
 * @date 2022/11/3
 */
@Primary
@Component
@AllArgsConstructor
public class DateSequenceStrategy implements OrderIdStrategy {

    private final SnowflakeGenerator generator;

    @Override
    public String generateOrderId(OrderSourceEnum source, BusinessTypeEnum type, TargetUser target) {
        var date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddssSSS"));
        var gen = String.valueOf(this.generator.nextId());
        gen = gen.substring(gen.length() - 8);
        return date + gen;
    }

    @Override
    public String generateSubOrderId(OrderSourceEnum source, BusinessTypeEnum type, String orderId, TargetUser target) {
        return null;
    }

}
