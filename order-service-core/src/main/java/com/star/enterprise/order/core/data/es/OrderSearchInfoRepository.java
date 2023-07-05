package com.star.enterprise.order.core.data.es;

import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2022/10/31
 */
public interface OrderSearchInfoRepository extends ElasticsearchRepository<OrderSearchInfoEntity, String> {

    /**
     * 根据订单id查找
     * @param orderId
     * @return
     */
    Optional<OrderSearchInfoEntity> findByOrderId(String orderId);

    /**
     * 根据订单id删除
     * @param orderId
     */
    void deleteByOrderId(String orderId);

}
