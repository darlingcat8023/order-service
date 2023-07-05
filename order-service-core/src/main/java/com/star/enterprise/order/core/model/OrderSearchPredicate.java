package com.star.enterprise.order.core.model;

import com.star.enterprise.order.base.Paired;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author xiaowenrou
 * @date 2022/11/1
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OrderSearchPredicate extends PermissionQuery {

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 订单创建时间
     */
    private Paired<Long, Long> createdDateRange;

    /**
     * 订单完成时间
     */
    private Paired<Long, Long> completedDateRange;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 付款方式
     */
    private String paymentMethod;

    /**
     * 业绩人
     */
    private String performanceUserId;

    /**
     * 制单人
     */
    private String createdByUserId;

    /**
     * 目标用户id
     */
    private String target;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 目标用户渠道
     */
    private Paired<String, String> targetChannel;

}
