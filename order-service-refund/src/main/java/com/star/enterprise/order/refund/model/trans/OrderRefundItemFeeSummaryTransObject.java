package com.star.enterprise.order.refund.model.trans;

import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundItemFeeEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Data
@Accessors(chain = true)
public class OrderRefundItemFeeSummaryTransObject {

    /**
     * 退费订单id
     */
    private String refundOrderId;

    /**
     * 退费项id
     */
    private String refundOrderItemId;

    /**
     * 收费订单id
     */
    private String orderId;

    /**
     * 订单物品id
     */
    private String orderItemId;

    /**
     * 退费时课程标准单价
     */
    private BigDecimal currentStandardPrice;

    /**
     * 当前余额
     */
    private BigDecimal currentBalance;

    /**
     * 退正课数量
     */
    private BigDecimal refundNumber;

    /**
     * 退赠送数量
     */
    private BigDecimal refundApportion;

    /**
     * 退款正课金额
     */
    private BigDecimal refundNumberPrice;

    /**
     * 退款赠课金额
     */
    private BigDecimal refundApportionPrice;

    /**
     * 退费溢价
     */
    private BigDecimal refundOverPrice;

    public OrderRefundItemFeeSummaryTransObject(OrderRefundItemFeeEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }

}
