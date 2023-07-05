package com.star.enterprise.order.receipt.handler;

import com.star.enterprise.order.core.model.EmployeeUser;
import com.star.enterprise.order.receipt.service.Receipt;
import com.star.enterprise.order.receipt.service.ReceiptService;

/**
 * 收据状态处理器
 * @author xiaowenrou
 * @date 2022/12/1
 */
public interface ReceiptStatusHandler {

    /**
     * 处理收据更新
     * @param service
     * @param receipt
     * @param operator
     * @param runnable
     */
    void processReceiptModify(ReceiptService<?> service, Receipt receipt, EmployeeUser operator, Runnable runnable);

    /**
     * 处理收据作废
     * @param service
     * @param receipt
     * @param operator
     * @param runnable
     */
    void processReceiptDiscard(ReceiptService<?> service, Receipt receipt, EmployeeUser operator, Runnable runnable);

}
