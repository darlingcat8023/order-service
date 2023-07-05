package com.star.enterprise.order.refund.data.es;

import com.star.enterprise.order.refund.data.es.entity.OrderRefundSearchInfoEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
public interface OrderRefundInfoSearchRepository extends ElasticsearchRepository<OrderRefundSearchInfoEntity, Long> {

    /**
     * 根据退费订单号查找
     * @param refundOrderId
     * @return
     */
    Optional<OrderRefundSearchInfoEntity> findByRefundOrderId(String refundOrderId);

}
