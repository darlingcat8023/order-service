package com.star.enterprise.order.charge.data.jpa;

import com.star.enterprise.order.charge.data.jpa.entity.ChargePropertyLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ChargePropertyLimitRepository extends JpaRepository<ChargePropertyLimitEntity, Long>, QuerydslPredicateExecutor<ChargePropertyLimitEntity> {

}
