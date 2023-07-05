package com.star.enterprise.order.refund.data.jpa;

import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundItemInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
public interface OrderRefundItemInfoRepository extends JpaRepository<OrderRefundItemInfoEntity, Long> {

    /**
     * 根据订单id查找
     * @param refundOrderId
     * @return
     */
    List<OrderRefundItemInfoEntity> findByRefundOrderId(String refundOrderId);

}
