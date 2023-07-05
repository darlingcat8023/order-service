package com.star.enterprise.order.core.data.jpa;

import com.star.enterprise.order.core.data.jpa.entity.OrderFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2022/9/22
 */
public interface OrderFeeRepository extends JpaRepository<OrderFeeEntity, Long> {

    /**
     * 根据订单id查找
     * @param orderId
     * @return
     */
    Optional<OrderFeeEntity> findByOrderId(String orderId);

    /**
     * 批量查询
     * @param orderId
     * @return
     */
    List<OrderFeeEntity> findByOrderIdIn(Collection<String> orderId);

}
