package com.star.enterprise.order.transfer.data.jpa;

import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferItemFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xiaowenrou
 * @date 2023/3/3
 */
public interface OrderTransferItemFeeRepository extends JpaRepository<OrderTransferItemFeeEntity, Long> {

}
