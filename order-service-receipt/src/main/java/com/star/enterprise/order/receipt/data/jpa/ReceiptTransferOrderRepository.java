package com.star.enterprise.order.receipt.data.jpa;

import com.star.enterprise.order.receipt.data.jpa.entity.ReceiptTransferOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2023/3/13
 */
public interface ReceiptTransferOrderRepository extends JpaRepository<ReceiptTransferOrderEntity, Long> {

    /**
     * 根据收据号查询
     * @param receiptNo
     * @return
     */
    Optional<ReceiptTransferOrderEntity> findByReceiptNo(String receiptNo);

    /**
     * 根据结转订单号查找
     * @param transferOrderId
     * @return
     */
    Optional<ReceiptTransferOrderEntity> findByTransferOrderId(String transferOrderId);

}
