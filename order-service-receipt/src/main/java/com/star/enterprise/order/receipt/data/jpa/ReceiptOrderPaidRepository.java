package com.star.enterprise.order.receipt.data.jpa;

import com.star.enterprise.order.receipt.data.jpa.entity.ReceiptOrderPaidEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2022/11/29
 */
public interface ReceiptOrderPaidRepository extends JpaRepository<ReceiptOrderPaidEntity, Long>, QuerydslPredicateExecutor<ReceiptOrderPaidEntity> {

    /**
     * 根据订单id查找
     * @param orderId
     * @return
     */
    Optional<ReceiptOrderPaidEntity> findByOrderId(String orderId);

    /**
     * 根据收据号查找
     * @param receiptNo
     * @return
     */
    Optional<ReceiptOrderPaidEntity> findByReceiptNo(String receiptNo);


}
