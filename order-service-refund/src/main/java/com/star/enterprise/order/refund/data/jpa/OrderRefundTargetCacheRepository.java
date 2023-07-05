package com.star.enterprise.order.refund.data.jpa;

import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundTargetCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
public interface OrderRefundTargetCacheRepository extends JpaRepository<OrderRefundTargetCacheEntity, Long> {


}
