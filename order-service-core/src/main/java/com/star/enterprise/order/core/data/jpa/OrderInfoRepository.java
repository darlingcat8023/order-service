package com.star.enterprise.order.core.data.jpa;

import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2022/9/22
 */
public interface OrderInfoRepository extends JpaRepository<OrderInfoEntity, Long>, QuerydslPredicateExecutor<OrderInfoEntity> {

    /**
     * 根据订单id查找
     * @param orderId
     * @return
     */
    Optional<OrderInfoEntity> findByOrderId(String orderId);

    /**
     * 批量查询
     * @param orderIds
     * @return
     */
    List<OrderInfoEntity> findByOrderIdIn(Collection<String> orderIds);

}
