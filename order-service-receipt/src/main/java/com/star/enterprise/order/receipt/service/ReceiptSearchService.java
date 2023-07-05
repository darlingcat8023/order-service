package com.star.enterprise.order.receipt.service;

import com.star.enterprise.order.receipt.data.es.entity.ReceiptSearchInfoEntity;
import com.star.enterprise.order.receipt.model.ReceiptSearchPredicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * 承载收据搜索服务
 * @author xiaowenrou
 * @date 2022/12/6
 */
public interface ReceiptSearchService {

    /**
     * 搜索收据数据
     * @param predicate
     * @param rollId
     * @param pageable
     * @return
     */
    Page<ReceiptSearchInfoEntity> searchReceipt(ReceiptSearchPredicate predicate, String rollId, Pageable pageable);

    /**
     * 刷新收据
     * @param receipt
     * @param operators
     */
    void refresh(Receipt receipt, Set<String> operators);

}
