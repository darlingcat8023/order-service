package com.star.enterprise.order.receipt.data.jpa;

import com.star.enterprise.order.receipt.model.ReceiptOperator;
import com.star.enterprise.order.receipt.data.jpa.entity.ReceiptLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/12/6
 */
public interface ReceiptLogRepository extends JpaRepository<ReceiptLogEntity, Long> {

    /**
     * 根据id查找
     * @param receiptNo
     * @return
     */
    List<ReceiptOperator> findAllByReceiptNo(String receiptNo);

}
