package com.star.enterprise.order.refund.data.jpa;

import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
public interface OrderRefundInfoRepository extends JpaRepository<OrderRefundInfoEntity, Long> {

    /**
     * 根据id查找
     * @param refundOrderId
     * @return
     */
    Optional<OrderRefundInfoEntity> findByRefundOrderId(String refundOrderId);

    /**
     * 获取退费信息
     * @param refundOrderIds
     * @return
     */
    List<OrderRefundInfoEntity> findByRefundOrderIdIn(Collection<String> refundOrderIds);


}
