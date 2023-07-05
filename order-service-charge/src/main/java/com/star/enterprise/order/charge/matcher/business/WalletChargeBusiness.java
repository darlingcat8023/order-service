package com.star.enterprise.order.charge.matcher.business;

import com.star.enterprise.order.charge.matcher.Business;

/**
 * @author xiaowenrou
 * @date 2023/4/19
 */
public interface WalletChargeBusiness extends Business {

    /**
     * 钱包id
     * @return
     */
    String walletId();

}
