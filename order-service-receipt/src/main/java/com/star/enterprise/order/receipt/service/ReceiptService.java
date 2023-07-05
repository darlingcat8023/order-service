package com.star.enterprise.order.receipt.service;

import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.model.EmployeeUser;
import com.star.enterprise.order.receipt.data.es.entity.ReceiptSearchInfoEntity;
import com.star.enterprise.order.receipt.model.ReceiptContext;

import java.util.Map;

/**
 * 收据服务
 * @author xiaowenrou
 * @date 2022/11/29
 */
public interface ReceiptService<T> {

    /**
     * 创建收据
     * @param orderId
     * @param target
     * @param context
     * @return
     */
    Receipt createReceipt(String orderId, TargetUser target, ReceiptContext context);

    /**
     * 修改收据
     * @param receiptNo
     * @param extendInfo
     * @param categories
     * @param operator
     */
    void modifyReceipt(String receiptNo, T extendInfo, Map<String, MatchResult> categories, EmployeeUser operator);

    /**
     * 作废收据
     * @param receiptNo
     * @param operator
     */
    void discardReceipt(String receiptNo, EmployeeUser operator);

    /**
     * 构建同步数据
     * @param receipt
     * @param entity
     */
    void buildSyncData(Receipt receipt, ReceiptSearchInfoEntity entity);

}
