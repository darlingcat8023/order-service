package com.star.enterprise.order.core.calculator;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
public interface OrderFeeDetail {

    /**
     * 业务时间
     * @return
     */
    LocalDateTime businessDate();

    /**
     * 订单来源
     * @return
     */
    String orderSource();

    /**
     * 优惠详情
     * @return
     */
    DiscountDetail discount();


    /**
     * 具体的费用项
     * @return
     */
    List<OrderItemFeeDetail> items();

    /**
     * 展示逻辑的上下文
     * @return
     */
    String webViewToast();

    /**
     * 获取分摊的数量
     * @return
     */
    default int additionalItemCount() {
       int c = 0;
       for (var d : items()) {
           if (d.additional()) {
               c++;
           }
       }
       return c;
    }

}
