package com.star.enterprise.order.core.data.jpa;

import com.star.enterprise.order.core.data.jpa.entity.OrderCouponCommitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2023/3/24
 */
public interface OrderCouponCommitRepository extends JpaRepository<OrderCouponCommitEntity, Long> {

    /**
     * 根据orderId查找
     * @param orderId
     * @return
     */
    Optional<OrderCouponCommitEntity> findByOrderId(String orderId);

}
