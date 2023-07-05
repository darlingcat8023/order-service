package com.star.enterprise.order.core.data.jpa;

import com.star.enterprise.order.core.data.jpa.entity.OrderItemInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2022/9/22
 */
public interface OrderItemInfoRepository extends JpaRepository<OrderItemInfoEntity, Long> {

    /**
     * 根据订单id查找
     * @param orderId
     * @return
     */
    List<OrderItemInfoEntity> findByOrderId(String orderId);

    /**
     * 批量查找
     * @param orderIds
     * @return
     */
    List<OrderItemInfoEntity> findByOrderIdIn(Collection<String> orderIds);

    /**
     * 根据订单id查找
     * @param orderId
     * @param businessId
     * @param businessType
     * @return
     */
    Optional<OrderItemInfoEntity> findByOrderIdAndBusinessIdAndBusinessType(String orderId, String businessId, String businessType);

    /**
     * 根据orderId删除
     * @param orderId
     */
    void deleteByOrderId(String orderId);

}
