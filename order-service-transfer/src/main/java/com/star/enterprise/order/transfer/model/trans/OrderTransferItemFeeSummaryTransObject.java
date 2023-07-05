package com.star.enterprise.order.transfer.model.trans;

import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferItemFeeEntity;
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
public class OrderTransferItemFeeSummaryTransObject {

    /**
     * 退费订单id
     */
    private String transferOrderId;

    /**
     * 退费项id
     */
    private String transferOrderItemId;

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

    private BigDecimal currentBalance;

    /**
     * 退正课数量
     */
    private BigDecimal transferNumber;

    /**
     * 退赠送数量
     */
    private BigDecimal transferApportion;

    /**
     * 退款正课金额
     */
    private BigDecimal transferNumberPrice;

    /**
     * 退款赠课金额
     */
    private BigDecimal transferApportionPrice;

    /**
     * 退费溢价
     */
    private BigDecimal transferOverPrice;

    public OrderTransferItemFeeSummaryTransObject(OrderTransferItemFeeEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }

}
