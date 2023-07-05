package com.star.enterprise.order.core.strategy;

import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.constants.OrderSourceEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 订单编号随机
 * @author xiaowenrou
 * @date 2022/9/22
 */
@Component
@AllArgsConstructor
public class RandomOrderIdStrategy implements OrderIdStrategy {

    @Override
    public String generateOrderId(OrderSourceEnum source, BusinessTypeEnum type, TargetUser target) {
        return UUID.randomUUID().toString();
    }

    @Override
    public String generateSubOrderId(OrderSourceEnum source, BusinessTypeEnum type, String orderId, TargetUser target) {
        return UUID.randomUUID().toString();
    }

}
