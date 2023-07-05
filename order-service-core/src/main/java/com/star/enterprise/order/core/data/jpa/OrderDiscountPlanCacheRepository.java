package com.star.enterprise.order.core.data.jpa;

import com.star.enterprise.order.core.data.jpa.entity.OrderDiscountPlanCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/10/25
 */
public interface OrderDiscountPlanCacheRepository extends JpaRepository<OrderDiscountPlanCacheEntity, Long> {

    /**
     * 根据orderId批量查找
     * @param orderId
     * @return
     */
    List<OrderDiscountPlanCacheEntity> findByOrderIdIn(Collection<String> orderId);

    /**
     * 根据订单id删除
     * @param orderId
     */
    void deleteByOrderId(String orderId);

}
