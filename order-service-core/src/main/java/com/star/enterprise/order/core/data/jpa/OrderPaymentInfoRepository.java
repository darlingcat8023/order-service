package com.star.enterprise.order.core.data.jpa;

import com.star.enterprise.order.core.data.jpa.entity.OrderPaymentInfoEntity;
import com.star.enterprise.order.core.model.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/10/25
 */
public interface OrderPaymentInfoRepository extends JpaRepository<OrderPaymentInfoEntity, Long> {

    /**
     * 根据orderId查找
     * @param orderId
     * @return
     */
    List<PaymentInfo> findByOrderId(String orderId);


    /**
     * 根据orderid批量查找
     * @param orderIds
     * @return
     */
    List<PaymentInfo> findByOrderIdIn(Collection<String> orderIds);

    /**
     * 删除orderId
     * @param orderId
     */
    void deleteByOrderId(String orderId);

}
