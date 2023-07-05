package com.star.enterprise.order.transfer.data.jpa;

import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
public interface OrderTransferFeeRepository extends JpaRepository<OrderTransferFeeEntity, Long> {

    /**
     * 根据订单号查找
     * @param transferOrderId
     * @return
     */
    Optional<OrderTransferFeeEntity> findByTransferOrderId(String transferOrderId);

    /**
     * 批量查询
     * @param transferOrderIds
     * @return
     */
    List<OrderTransferFeeEntity> findByTransferOrderIdIn(Collection<String> transferOrderIds);

}
