package com.star.enterprise.order.receipt.strategy;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.receipt.constants.ReceiptTypeEnum;

/**
 * 收据编号生成策略
 * @author xiaowenrou
 * @date 2022/12/1
 */
public interface ReceiptNoStrategy {

    /**
     * 生成收据号
     * @param target
     * @param orderId
     * @param type
     * @return
     */
    String generateReceiptNo(TargetUser target, String orderId, ReceiptTypeEnum type);

}
