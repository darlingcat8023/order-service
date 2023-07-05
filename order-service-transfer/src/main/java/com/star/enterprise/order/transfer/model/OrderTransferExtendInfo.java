package com.star.enterprise.order.transfer.model;

/**
 * @author xiaowenrou
 * @date 2023/3/6
 */
public interface OrderTransferExtendInfo extends OrderTransferInfo {

    /**
     * 附加信息
     * @return
     */
    TransferExtendInfo extendInfo();

}
