package com.star.enterprise.order.core.strategy;

import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.constants.OrderSourceEnum;

/**
 * 订单编号生成策略
 * @author xiaowenrou
 * @date 2022/9/22
 */
public interface OrderIdStrategy {

    /**
     * 生成订单id
     * @param source
     * @param type
     * @return
     */
    String generateOrderId(OrderSourceEnum source, BusinessTypeEnum type, TargetUser target);

    /**
     * 生成子订单id
     * @param source
     * @param type
     * @param orderId
     * @return
     */
    String generateSubOrderId(OrderSourceEnum source, BusinessTypeEnum type, String orderId, TargetUser target);

}
