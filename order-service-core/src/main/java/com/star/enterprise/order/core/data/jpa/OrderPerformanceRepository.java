package com.star.enterprise.order.core.data.jpa;

import com.star.enterprise.order.core.data.jpa.entity.OrderPerformanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/9/22
 */
public interface OrderPerformanceRepository extends JpaRepository<OrderPerformanceEntity, Long> {

    /**
     *
     * @param orderId
     * @return
     */
    List<OrderPerformanceEntity> findByOrderId(String orderId);

    /**
     * 根据orderId批量查找
     * @param orderIds
     * @return
     */
    List<OrderPerformanceEntity> findByOrderIdIn(Collection<String> orderIds);

    /**
     * 删除orderId
     * @param orderId
     */
    void deleteByOrderId(String orderId);

}
