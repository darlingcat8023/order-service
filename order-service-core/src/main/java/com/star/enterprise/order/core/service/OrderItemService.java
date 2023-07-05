package com.star.enterprise.order.core.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.core.data.jpa.OrderItemLeftRepository;
import com.star.enterprise.order.core.data.jpa.entity.OrderItemLeftEntity;
import com.star.enterprise.order.core.data.jpa.entity.QOrderItemFeeEntity;
import com.star.enterprise.order.core.data.jpa.entity.QOrderItemInfoEntity;
import com.star.enterprise.order.core.model.trans.OrderItemFeeSummaryTransObject;
import com.star.enterprise.order.core.model.trans.OrderItemSummaryTransObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static com.star.enterprise.order.base.exception.RestCode.CORE_DATA_VERSION;

/**
 * @author xiaowenrou
 * @date 2023/4/3
 */
@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemLeftRepository orderItemLeftRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<OrderItemSummaryTransObject> getOrderItemTransObject(String orderItemId) {
        var query = new JPAQuery<>(this.entityManager);
        QOrderItemInfoEntity qoi = QOrderItemInfoEntity.orderItemInfoEntity;
        QOrderItemFeeEntity qof = QOrderItemFeeEntity.orderItemFeeEntity;
        var tuple = query.select(qoi, qof).from(qoi).leftJoin(qof).on(qoi.orderItemId.eq(qof.orderItemId)).where(qoi.orderItemId.eq(orderItemId)).stream().findFirst();
        return tuple.map(t -> new OrderItemSummaryTransObject(t.get(qoi), new OrderItemFeeSummaryTransObject(t.get(qof))));
    }

    public Optional<OrderItemLeftEntity> getOrderItemLeft(String orderItemId) {
        return this.orderItemLeftRepository.findByOrderItemId(orderItemId);
    }

    public void modifyOrderItemLeft(OrderItemLeftEntity projection) {
        this.orderItemLeftRepository.findByOrderItemId(projection.getOrderItemId()).ifPresent(item -> {
            if (!item.getVersion().equals(projection.getVersion())) {
                throw new BusinessWarnException(CORE_DATA_VERSION, "error.order.dataVersion");
            }
            this.orderItemLeftRepository.saveAndFlush(item.setNumberLeft(projection.getNumberLeft()).setApportionLeft(projection.getApportionLeft()));
        });
    }

}
