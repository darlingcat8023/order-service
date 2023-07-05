package com.star.enterprise.order.receipt.service;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.receipt.data.es.entity.ReceiptSearchInfoEntity;
import com.star.enterprise.order.receipt.model.ReceiptSearchPredicate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.RestStatusException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.star.enterprise.order.base.exception.RestCode.CORE_DEEP_DATA;

/**
 * @author xiaowenrou
 * @date 2023/3/14
 */
@Slf4j
@Primary
@Service
@AllArgsConstructor
public class EmptyReceiptSearchWrapper implements ReceiptSearchService {

    private final ReceiptElasticService delegate;

    @Override
    public Page<ReceiptSearchInfoEntity> searchReceipt(ReceiptSearchPredicate predicate, String rollId, Pageable pageable) {
        try {
            if (pageable.getOffset() > 5000L) {
                throw new BusinessWarnException(CORE_DEEP_DATA, "error.order.deepData");
            }
            return this.delegate.searchReceipt(predicate, rollId, pageable);
        } catch (RestStatusException e) {
            return new PageImpl<>(List.of(), pageable, 0L);
        }
    }

    @Override
    public void refresh(Receipt receipt, Set<String> operators) {
        this.delegate.refresh(receipt, operators);
    }

}
