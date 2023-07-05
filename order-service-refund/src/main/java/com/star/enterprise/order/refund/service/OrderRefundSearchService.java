package com.star.enterprise.order.refund.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.refund.data.es.entity.OrderRefundSearchInfoEntity;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundInfoEntity;
import com.star.enterprise.order.refund.model.RefundOrderSearchPredicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
public interface OrderRefundSearchService {

    /**
     * 分页查询退费订单数据
     * @param predicate
     * @param rollId
     * @param pageable
     * @return
     */
    Page<OrderRefundSearchInfoEntity> pageRefundOrder(RefundOrderSearchPredicate predicate, String rollId, Pageable pageable);

    /**
     * 刷新退费订单数据
     * @param refundOrderId
     * @param target
     * @param orderRefund
     */
    void refresh(String refundOrderId, TargetUser target, OrderRefundInfoEntity orderRefund);

}
