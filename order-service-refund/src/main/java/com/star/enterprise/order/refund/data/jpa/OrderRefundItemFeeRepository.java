package com.star.enterprise.order.refund.data.jpa;

import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundItemFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
public interface OrderRefundItemFeeRepository extends JpaRepository<OrderRefundItemFeeEntity, Long> {

}
