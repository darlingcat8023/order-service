package com.star.enterprise.order.transfer.data.jpa;

import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferItemInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
public interface OrderTransferItemInfoRepository extends JpaRepository<OrderTransferItemInfoEntity, Long> {

    /**
     * 根据订单id查找
     * @param refundOrderId
     * @return
     */
    List<OrderTransferItemInfoEntity> findByTransferOrderId(String refundOrderId);

}
