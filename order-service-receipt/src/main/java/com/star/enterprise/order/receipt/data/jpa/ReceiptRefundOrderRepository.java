package com.star.enterprise.order.receipt.data.jpa;

import com.star.enterprise.order.receipt.data.jpa.entity.ReceiptRefundOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2023/3/13
 */
public interface ReceiptRefundOrderRepository extends JpaRepository<ReceiptRefundOrderEntity, Long> {

    /**
     * 根据收据号查询
     * @param receiptNo
     * @return
     */
    Optional<ReceiptRefundOrderEntity> findByReceiptNo(String receiptNo);

    /**
     * 根据收据号查询
     * @param refundOrderId
     * @return
     */
    Optional<ReceiptRefundOrderEntity> findByRefundOrderId(String refundOrderId);

}
