package com.star.enterprise.order.refund.data.jpa;

import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundWalletCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xiaowenrou
 * @date 2023/3/16
 */
public interface OrderRefundWalletCacheRepository extends JpaRepository<OrderRefundWalletCacheEntity, Long> {
}
