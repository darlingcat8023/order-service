package com.star.enterprise.order.http.order.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public record OrderItemFeeResponseRecord(

        BigDecimal originalSinglePrice,

        BigDecimal originalTotalPrice,

        BigDecimal afterDiscountSinglePrice,

        BigDecimal afterDiscountTotalPrice,

        DiscountPlanResponseRecord discountPlanRecord,

        BigDecimal useDirect,

        BigDecimal useWallet,

        BigDecimal dueCollectPrice,

        List<ChargeMatchResultResponseRecord> calculatedChargeCategory

) {}
