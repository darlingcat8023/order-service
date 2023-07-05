package com.star.enterprise.order.receipt.strategy;

import com.star.enterprise.order.base.utils.SnowflakeGenerator;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.receipt.constants.ReceiptTypeEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xiaowenrou
 * @date 2022/12/1
 */
@Component
@AllArgsConstructor
public class SequenceReceiptNoStrategy implements ReceiptNoStrategy {

    private final SnowflakeGenerator generator;

    @Override
    public String generateReceiptNo(TargetUser target, String orderId, ReceiptTypeEnum type) {
        return String.valueOf(generator.nextId());
    }

}
