package com.star.enterprise.order.transfer.data.es;

import com.star.enterprise.order.transfer.data.es.entity.OrderTransferSearchInfoEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
public interface OrderTransferInfoSearchRepository extends ElasticsearchRepository<OrderTransferSearchInfoEntity, Long> {

    /**
     * 根据退费订单号查找
     * @param transferOrderId
     * @return
     */
    Optional<OrderTransferSearchInfoEntity> findByTransferOrderId(String transferOrderId);

}
