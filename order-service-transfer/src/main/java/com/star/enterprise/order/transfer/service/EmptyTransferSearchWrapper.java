package com.star.enterprise.order.transfer.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.transfer.data.es.entity.OrderTransferSearchInfoEntity;
import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferInfoEntity;
import com.star.enterprise.order.transfer.model.TransferOrderSearchPredicate;
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
public class EmptyTransferSearchWrapper implements OrderTransferSearchService {

    private final OrderTransferElasticsearchService delegate;

    @Override
    public Page<OrderTransferSearchInfoEntity> pageTransferOrder(TransferOrderSearchPredicate predicate, String rollId, Pageable pageable) {
        try {
            return this.delegate.pageTransferOrder(predicate, rollId, pageable);
        } catch (RestStatusException e) {
            return new PageImpl<>(List.of(), pageable, 0L);
        }
    }

    @Override
    public void refresh(String transferOrderId, TargetUser target, OrderTransferInfoEntity orderTransfer) {
        this.delegate.refresh(transferOrderId, target, orderTransfer);
    }

}
