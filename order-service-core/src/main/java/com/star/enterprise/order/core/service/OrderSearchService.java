package com.star.enterprise.order.core.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.model.OrderSearchPredicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author xiaowenrou
 * @date 2022/10/31
 */
public interface OrderSearchService {

    /**
     * 根据订单条件查询
     * @param predicate
     * @return
     */
    Page<OrderSearchInfoEntity> searchOrder(OrderSearchPredicate predicate, String rollId, Pageable pageable);

    /**
     * 刷新订单数据
     * @param orderId
     * @param target
     * @param order
     */
    void refresh(String orderId, TargetUser target, OrderInfoEntity order);

}
