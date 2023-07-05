package com.star.enterprise.order.transfer.model;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public interface OrderTransferInfo {

    /**
     * 退款项
     * @return
     */
    List<OrderTransferItemInfo> items();

    /**
     * 附加费用
     * @return
     */
    TransferAdditionalFee additionalFee();

}
