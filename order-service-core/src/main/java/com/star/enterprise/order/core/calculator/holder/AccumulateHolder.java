package com.star.enterprise.order.core.calculator.holder;

import com.star.enterprise.order.core.calculator.CalculatorDelayTask;

import java.math.BigDecimal;
import java.util.List;

/**
 * 累加器
 * @author xiaowenrou
 * @date 2022/9/23
 */
public interface AccumulateHolder {

    /**
     * 使用优惠券金额
     * @return
     */
    BigDecimal getUseCoupons();

    /**
     * 订单总金额
     * @return
     */
    BigDecimal getOrderTotalPrice();

    /**
     * 订单折后总金额
     * @return
     */
    BigDecimal getOrderAfterDiscountTotalPrice();

    /**
     * 订单实收金额
     * @return
     */
    BigDecimal getOrderDueCollectPrice();

    /**
     * 使用优惠券金额
     * @return
     */
    void addUseCoupons(BigDecimal price);

    /**
     * set
     * @param price
     * @return
     */
    void addOrderTotalPrice(BigDecimal price);

    /**
     * set
     * @param decimal
     * @return
     */
    void addOrderAfterDiscountTotalPrice(BigDecimal decimal);


    /**
     * + 订单实收金额
     * @param decimal
     * @return
     */
    void addOrderDueCollectPrice(BigDecimal decimal);

    /**
     * set
     * @param decimal
     * @return
     */
    void subtractOrderDueCollectPrice(BigDecimal decimal);

    /**
     * 添加延迟任务
     * @param delayTask
     * @return
     */
    void addDelayTask(CalculatorDelayTask delayTask);

    /**
     * 获取延迟任务
     * @return
     */
    List<CalculatorDelayTask> getDelayTasks();

}
