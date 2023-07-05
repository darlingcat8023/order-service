package com.star.enterprise.order.core.data.jpa;

import com.star.enterprise.order.core.data.jpa.entity.OrderItemLeftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2023/4/3
 */
public interface OrderItemLeftRepository extends JpaRepository<OrderItemLeftEntity, Long> {

    /**
     * 根据订单项id查找
     * @param orderItemId
     * @return
     */
    Optional<OrderItemLeftEntity> findByOrderItemId(String orderItemId);

}
