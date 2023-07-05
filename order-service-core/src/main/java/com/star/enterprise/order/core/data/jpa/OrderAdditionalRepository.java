package com.star.enterprise.order.core.data.jpa;

import com.star.enterprise.order.core.data.jpa.entity.OrderAdditionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2022/9/22
 */
public interface OrderAdditionalRepository extends JpaRepository<OrderAdditionalEntity, Long> {

    /**
     * 根据订单id查找
     * @param orderId
     * @return
     */
    Optional<OrderAdditionalEntity> findByOrderId(String orderId);

    /**
     * 批量查询
     * @param orderIds
     * @return
     */
    List<OrderAdditionalEntity> findByOrderIdIn(Collection<String> orderIds);

    /**
     * 删除
     * @param orderId
     */
    void deleteByOrderId(String orderId);

}
