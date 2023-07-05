package com.star.enterprise.order.receipt.data.jpa;

import com.star.enterprise.order.receipt.data.jpa.entity.ReceiptSequenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @author xiaowenrou
 * @date 2022/12/1
 */
public interface ReceiptSequenceRepository extends JpaRepository<ReceiptSequenceEntity, Long>, QuerydslPredicateExecutor<ReceiptSequenceEntity> {

    /**
     * 删除
     * @param receiptNo
     */
    void deleteByReceiptNo(String receiptNo);

}
