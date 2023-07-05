package com.star.enterprise.order.transfer.data.jpa;

import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferTargetCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
public interface OrderTransferTargetCacheRepository extends JpaRepository<OrderTransferTargetCacheEntity, Long> {


}
