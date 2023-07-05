package com.star.enterprise.order.receipt.service;

import com.star.enterprise.order.core.model.EmployeeUser;
import com.star.enterprise.order.receipt.model.ReceiptOperator;
import com.star.enterprise.order.receipt.constants.ReceiptActionEnum;

import java.util.List;
import java.util.Set;

/**
 * @author xiaowenrou
 * @date 2022/12/6
 */
public interface ReceiptLogService {

    /**
     * 添加log
     * @param receipt
     * @param operator
     * @param action
     */
    void addReceiptLog(Receipt receipt, EmployeeUser operator, ReceiptActionEnum action);

    /**
     * 获取所有的log
     * @param receiptNo
     * @return
     */
    Set<String> distinctOperator(String receiptNo);

    /**
     * 列出操作人
     * @param receiptNo
     * @return
     */
    List<ReceiptOperator> listOperators(String receiptNo);

}
