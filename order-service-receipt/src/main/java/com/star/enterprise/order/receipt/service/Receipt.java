package com.star.enterprise.order.receipt.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.receipt.constants.ReceiptStatusEnum;
import com.star.enterprise.order.receipt.constants.ReceiptTypeEnum;

/**
 * @author xiaowenrou
 * @date 2022/12/1
 */
public interface Receipt {

    String receiptNo();

    String orderId();

    TargetUser target();

    ReceiptStatusEnum status();

    ReceiptTypeEnum type();

    Integer print();

}
