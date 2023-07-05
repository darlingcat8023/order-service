package com.star.enterprise.order.charge.model;

import com.star.enterprise.order.charge.matcher.ChargeHook;

import java.math.BigDecimal;
import java.util.List;

/**
 * 费用明细接口
 * @author xiaowenrou
 * @date 2022/9/23
 */
public interface Fee extends DiscountFee {

    /**
     * 原单价
     * @return
     */
    BigDecimal getOriginalSinglePrice();

    /**
     * 原单价
     * @return
     */
    BigDecimal getOriginalTotalPrice();

    /**
     * 折后单价
     * @return
     */
    BigDecimal getAfterDiscountSinglePrice();

    /**
     * 折后单价
     * @return
     */
    BigDecimal getAfterDiscountTotalPrice();

    /**
     * 应收金额
     * @return
     */
    BigDecimal getDueCollectSinglePrice();


    /**
     * 应收金额
     * @return
     */
    BigDecimal getDueCollectPrice();


    /**
     * 添加回调事件
     * @param hook
     */
    void addHooks(ChargeHook hook);

    /**
     * 获取所有回调事件
     * @return
     */
    List<ChargeHook> getHooks();

}
