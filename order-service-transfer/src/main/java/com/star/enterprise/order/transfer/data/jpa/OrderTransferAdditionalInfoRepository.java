package com.star.enterprise.order.transfer.data.jpa;

import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferAdditionInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
public interface OrderTransferAdditionalInfoRepository extends JpaRepository<OrderTransferAdditionInfoEntity, Long> {

    /**
     * 批量查找
     * @param transferOrderIds
     * @return
     */
    List<OrderTransferAdditionInfoEntity> findByTransferOrderIdIn(Collection<String> transferOrderIds);

    /**
     * 根据结转订单号删除
     * @param transferOrderId
     */
    void deleteByTransferOrderId(String transferOrderId);

}
