package com.star.enterprise.order.receipt.data.es;

import com.star.enterprise.order.receipt.data.es.entity.ReceiptSearchInfoEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2023/3/13
 */
public interface ReceiptSearchInfoRepository extends ElasticsearchRepository<ReceiptSearchInfoEntity, Long> {

    /**
     * 根据 receiptNo 查询
     * @param receiptNo
     * @return
     */
    Optional<ReceiptSearchInfoEntity> findByReceiptNo(String receiptNo);

}
