package com.star.enterprise.order.refund.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.refund.data.es.entity.OrderRefundSearchInfoEntity;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundInfoEntity;
import com.star.enterprise.order.refund.model.RefundOrderSearchPredicate;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.RestStatusException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/14
 */
@Primary
@Service
@AllArgsConstructor
public class EmptyRefundSearchWrapper implements OrderRefundSearchService {

    private final OrderRefundElasticsearchService delegate;

    @Override
    public Page<OrderRefundSearchInfoEntity> pageRefundOrder(RefundOrderSearchPredicate predicate, String rollId, Pageable pageable) {
        try {
            return this.delegate.pageRefundOrder(predicate, rollId, pageable);
        } catch (RestStatusException e) {
            return new PageImpl<>(List.of(), pageable, 0L);
        }
    }

    @Override
    public void refresh(String refundOrderId, TargetUser target, OrderRefundInfoEntity orderRefund) {
        this.delegate.refresh(refundOrderId, target, orderRefund);
    }

}
