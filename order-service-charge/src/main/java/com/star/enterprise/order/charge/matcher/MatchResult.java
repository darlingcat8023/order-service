package com.star.enterprise.order.charge.matcher;

/**
 * @author xiaowenrou
 * @date 2023/1/5
 */
public interface MatchResult {

    /**
     * id
     * @return
     */
    String chargeItemId();

    /**
     * 类型
     * @return
     */
    String chargeCategory();

}
