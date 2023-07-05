package com.star.enterprise.order.transfer.data.jpa;

import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
public interface OrderTransferInfoRepository extends JpaRepository<OrderTransferInfoEntity, Long> {

    /**
     * 根据id查找
     * @param transferOrderId
     * @return
     */
    Optional<OrderTransferInfoEntity> findByTransferOrderId(String transferOrderId);

    /**
     * 获取退费信息
     * @param transferOrderIds
     * @return
     */
    List<OrderTransferInfoEntity> findByTransferOrderIdIn(Collection<String> transferOrderIds);


}
