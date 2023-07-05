package com.star.enterprise.order.core.data.jpa;

import com.star.enterprise.order.core.data.jpa.entity.OrderCouponBindEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/11/7
 */
public interface OrderCouponBindRepository extends JpaRepository<OrderCouponBindEntity, Long> {

    /**
     * 根据orderid查找
     * @param orderId
     * @return
     */
    List<OrderCouponBindEntity> findByOrderId(String orderId);

    /**
     * 根据orderId批量查找
     * @param orderIds
     * @return
     */
    List<OrderCouponBindEntity> findByOrderIdIn(Collection<String> orderIds);

    /**
     * 根据orderId删除
     * @param orderId
     */
    void deleteByOrderId(String orderId);

}
