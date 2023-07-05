package com.star.enterprise.order.core.calculator;

import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.core.model.OrderBusinessInfo;

/**
 * @author xiaowenrou
 * @date 2022/9/23
 */
public interface OrderItemFeeDetail extends OrderBusinessInfo {

    /**
     * 使用分摊
     * @return
     */
    boolean additional();

    /**
     * 购买数量
     * @return
     */
    Integer number();

    /**
     * 赠送数量
     * @return
     */
    int apportion();

    /**
     * 父id
     * @return
     */
    String parentBusinessId();

    /**
     * 折扣方案
     * @return
     */
    DiscountPlanDetail discountPlan();

    /**
     * 回调地址
     * @return
     */
    String callbackAddress();

    /**
     * 费用类型
     * @return
     */
    MatchResult chargeCategory();

    /**
     * 操作
     * @return
     */
    OrderItemFeeOperator operator();

    /**
     * 上下文
     * @return
     */
    OrderItemContext context();

}
