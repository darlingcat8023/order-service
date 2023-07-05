package com.star.enterprise.order.receipt.service;

import com.star.enterprise.order.charge.model.TargetUser;

import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2022/12/21
 */
public interface ReceiptSequenceService {

    /**
     * 添加序列
     * @param receipt
     * @param target
     */
    void addReceiptSequence(Receipt receipt, TargetUser target);

    /**
     * 最后一个收据
     * @return
     */
    Optional<String> latestReceiptNo(TargetUser target);

    void deleteReceiptSequence(String receiptNo);

}
