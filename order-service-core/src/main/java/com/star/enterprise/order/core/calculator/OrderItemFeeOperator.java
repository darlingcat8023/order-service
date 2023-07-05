package com.star.enterprise.order.core.calculator;

import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.charge.model.Fee;

import java.math.BigDecimal;
import java.util.List;

/**
 * 费用操作接口
 * @author xiaowenrou
 * @date 2022/9/23
 */
public interface OrderItemFeeOperator extends Fee {

    /**
     * 原始单价
     * @param decimal
     * @return
     */
    OrderItemFeeOperator setOriginalSinglePrice(BigDecimal decimal);

    /**
     * 原总价
     * @return
     */
    OrderItemFeeOperator setOriginalTotalPrice(BigDecimal decimal);

    /**
     * 折后单价
     * @param decimal
     * @return
     */
    OrderItemFeeOperator setAfterDiscountSinglePrice(BigDecimal decimal);

    /**
     * 折后总价
     * @param decimal
     * @return
     */
    OrderItemFeeOperator setAfterDiscountTotalPrice(BigDecimal decimal);

    /**
     * 使用直减
     * @param decimal
     * @return
     */
    OrderItemFeeOperator setUseDirect(BigDecimal decimal);

    /**
     * 使用优惠券
     * @param decimal
     * @return
     */
    OrderItemFeeOperator setUseCoupons(BigDecimal decimal);

    /**
     * 使用电子钱包
     * @param decimal
     * @return
     */
    OrderItemFeeOperator setUseWallet(BigDecimal decimal);

    /**
     * 应收金额
     * @param decimal
     * @return
     */
    OrderItemFeeOperator setDueCollectSinglePrice(BigDecimal decimal);

    /**
     * 应收金额
     * @param decimal
     * @return
     */
    OrderItemFeeOperator setDueCollectPrice(BigDecimal decimal);

    /**
     * 添加费用类型
     * @param category
     * @return
     */
    OrderItemFeeOperator addChargeCategory(MatchResult category);

    /**
     * 添加费用类型
     * @param cats
     * @return
     */
    default OrderItemFeeOperator addChargeCategory(List<MatchResult> cats) {
        cats.forEach(this::addChargeCategory);
        return this;
    }

    List<MatchResult> getCalculatedChargeCategory();

}
