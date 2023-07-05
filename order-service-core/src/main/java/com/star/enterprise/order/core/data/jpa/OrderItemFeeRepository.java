package com.star.enterprise.order.core.data.jpa;

import com.star.enterprise.order.core.data.jpa.entity.OrderItemFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2022/9/22
 */
public interface OrderItemFeeRepository extends JpaRepository<OrderItemFeeEntity, Long> {

    /**
     * 根据orderId 查找
     * @param orderId
     * @return
     */
    List<OrderItemFeeEntity> findByOrderId(String orderId);

    /**
     * 根据orderId 和 orderItemId 查找
     * @param orderId
     * @param orderItemId
     * @return
     */
    Optional<OrderItemFeeEntity> findByOrderIdAndOrderItemId(String orderId, String orderItemId);

    /**
     * 根据orderId删除
     * @param orderId
     */
    void deleteByOrderId(String orderId);

}
