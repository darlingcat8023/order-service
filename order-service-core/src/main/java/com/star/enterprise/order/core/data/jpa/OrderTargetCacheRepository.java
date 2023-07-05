package com.star.enterprise.order.core.data.jpa;

import com.star.enterprise.order.core.data.jpa.entity.OrderTargetCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2022/11/1
 */
public interface OrderTargetCacheRepository extends JpaRepository<OrderTargetCacheEntity, Long> {

    /**
     * 根据orderId查找
     * @param orderId
     * @return
     */
    Optional<OrderTargetCacheEntity> findByOrderId(String orderId);

}
