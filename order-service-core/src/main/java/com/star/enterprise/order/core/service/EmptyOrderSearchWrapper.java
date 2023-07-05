package com.star.enterprise.order.core.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.model.OrderSearchPredicate;
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
public class EmptyOrderSearchWrapper implements OrderSearchService {

    private final OrderElasticsearchService delegate;

    @Override
    public Page<OrderSearchInfoEntity> searchOrder(OrderSearchPredicate predicate, String rollId, Pageable pageable) {
        try {
            return this.delegate.searchOrder(predicate, rollId, pageable);
        } catch (RestStatusException e) {
            return new PageImpl<>(List.of(), pageable, 0L);
        }
    }

    @Override
    public void refresh(String orderId, TargetUser target, OrderInfoEntity order) {
        this.delegate.refresh(orderId, target, order);
    }

}
