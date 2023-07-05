package com.star.enterprise.order.refund.data.jpa;

import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundAdditionInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
public interface OrderRefundAdditionalInfoRepository extends JpaRepository<OrderRefundAdditionInfoEntity, Long> {

    /**
     * 批量查找
     * @param refundOrderIds
     * @return
     */
    List<OrderRefundAdditionInfoEntity> findByRefundOrderIdIn(Collection<String> refundOrderIds);

    /**
     * 根据id删除
     * @param refundOrderId
     */
    void deleteByRefundOrderId(String refundOrderId);

}
