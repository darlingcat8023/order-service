package com.star.enterprise.order.transfer.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.transfer.data.es.entity.OrderTransferSearchInfoEntity;
import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferInfoEntity;
import com.star.enterprise.order.transfer.model.TransferOrderSearchPredicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
public interface OrderTransferSearchService {

    /**
     * 分页查询退费订单数据
     * @param predicate
     * @param rollId
     * @param pageable
     * @return
     */
    Page<OrderTransferSearchInfoEntity> pageTransferOrder(TransferOrderSearchPredicate predicate, String rollId, Pageable pageable);

    /**
     * 刷新退费订单数据
     * @param transferOrderId
     * @param target
     * @param orderTransfer
     */
    void refresh(String transferOrderId, TargetUser target, OrderTransferInfoEntity orderTransfer);

}
