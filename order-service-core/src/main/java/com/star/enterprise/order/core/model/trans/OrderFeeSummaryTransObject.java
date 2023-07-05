package com.star.enterprise.order.core.model.trans;

import com.star.enterprise.order.core.data.jpa.entity.OrderFeeEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/10/26
 */
@Data
@Accessors(chain = true)
public class OrderFeeSummaryTransObject {

    /**
     * 原总价
     */
    private BigDecimal orderOriginPrice;

    /**
     * 折后总价
     */
    private BigDecimal orderAfterDiscountPrice;

    /**
     * 使用直减金额
     */
    private BigDecimal useDirect;

    /**
     * 使用优惠券
     */
    private BigDecimal useCoupons;

    /**
     * 使用零钱包的金额
     */
    private BigDecimal useWallet;

    /**
     * 应实金额
     */
    private BigDecimal orderDueCollectPrice;

    public OrderFeeSummaryTransObject(OrderFeeEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }
}
