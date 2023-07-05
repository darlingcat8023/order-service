package com.star.enterprise.order.charge.matcher;

import com.star.enterprise.order.charge.model.Fee;

/**
 * 商品类型的父接口
 * @author xiaowenrou
 * @date 2022/9/22
 */
public interface Business {

    /**
     * 唯一id
     * @return
     */
    String uniqueId();

    /**
     * 费用明细
     * @return
     */
    Fee fee();

}
