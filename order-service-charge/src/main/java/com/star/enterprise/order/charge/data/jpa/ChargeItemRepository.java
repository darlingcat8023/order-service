package com.star.enterprise.order.charge.data.jpa;

import com.star.enterprise.order.charge.data.jpa.entity.ChargeItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2022/9/14
 */
public interface ChargeItemRepository extends JpaRepository<ChargeItemEntity, Long>, QuerydslPredicateExecutor<ChargeItemEntity> {

    /**
     * 根据chargeItemId查找
     * @param chargeItemId
     * @return
     */
    Optional<ChargeItemEntity> findByChargeItemId(String chargeItemId);

}