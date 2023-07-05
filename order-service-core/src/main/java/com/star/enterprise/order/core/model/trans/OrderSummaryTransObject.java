package com.star.enterprise.order.core.model.trans;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.model.PaymentInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2022/10/26
 */
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class OrderSummaryTransObject {

    private final String orderId;

    private LocalDateTime createdAt;

    private LocalDateTime completedDate;

    private LocalDateTime businessDate;

    private String orderSource;

    private String webViewToast;

    private String status;

    private List<OrderItemSummaryTransObject> items;

    private OrderFeeSummaryTransObject orderFee;

    private OrderAdditionalSummaryTransObject additional;

    private List<PaymentInfo> payments;

    private TargetUser target;

    private List<OrderCouponTransObject> coupons;

    private Map<String, Object> context = new HashMap<>();

    public OrderSummaryTransObject(OrderInfoEntity entity) {
        this(entity.getOrderId());
        BeanUtils.copyProperties(entity, this);
    }

}
