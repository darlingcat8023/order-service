package com.star.enterprise.order.remote.callback;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/2/10
 */
public record CoursePaidCallBackRequest (

        /*
          订单id
         */
        String orderId,

        /*
          订单项id
         */
        String orderItemId,

        /*
         * 业务id
         */
        String businessId,

        /*
         * 业务名
         */
        String businessName,

        /*
         * 购买数量
         */
        BigDecimal number,

        /*
         * 购买单价
         */
        BigDecimal apportion,

        BusinessFeeRecord fee,

        ExpireStrategyRecord expireStrategy,

        TargetRecord target


) {}
