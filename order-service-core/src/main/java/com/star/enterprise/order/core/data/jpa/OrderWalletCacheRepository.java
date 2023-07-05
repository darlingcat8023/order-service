package com.star.enterprise.order.core.data.jpa;

import com.star.enterprise.order.core.data.jpa.entity.OrderWalletCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2022/10/26
 */
public interface OrderWalletCacheRepository extends JpaRepository<OrderWalletCacheEntity, Long> {

    /**
     * 根据orderId查找
     * @param orderId
     * @return
     */
    Optional<OrderWalletCacheEntity> findByOrderId(String orderId);

}
