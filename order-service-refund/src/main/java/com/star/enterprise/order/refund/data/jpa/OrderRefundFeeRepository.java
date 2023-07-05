package com.star.enterprise.order.refund.data.jpa;

import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
public interface OrderRefundFeeRepository extends JpaRepository<OrderRefundFeeEntity, Long> {

    /**
     * 根据订单号查找
     * @param refundOrderId
     * @return
     */
    Optional<OrderRefundFeeEntity> findByRefundOrderId(String refundOrderId);

    /**
     * 批量查询
     * @param refundOrderIds
     * @return
     */
    List<OrderRefundFeeEntity> findByRefundOrderIdIn(Collection<String> refundOrderIds);

}
