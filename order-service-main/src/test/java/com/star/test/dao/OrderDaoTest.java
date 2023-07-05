package com.star.test.dao;

import com.querydsl.jpa.impl.JPAQuery;
import com.star.enterprice.orderservice.OrderServiceApplication;
import com.star.enterprise.order.core.constants.OrderStatusEnum;
import com.star.enterprise.order.core.data.jpa.OrderInfoRepository;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.data.jpa.entity.QOrderItemFeeEntity;
import com.star.enterprise.order.core.data.jpa.entity.QOrderItemInfoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest(classes = {OrderServiceApplication.class})
public class OrderDaoTest {

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Test
    public void testVersion() {
        OrderInfoEntity byOrderId = this.orderInfoRepository.findByOrderId("202212215278519508736").get();
        byOrderId.setStatus(OrderStatusEnum.TO_BE_PAID.value());
        var fi = this.orderInfoRepository.saveAndFlush(byOrderId);
        System.out.println(fi);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testDsl() {
        var query = new JPAQuery<>(this.entityManager);
        QOrderItemInfoEntity qoi = QOrderItemInfoEntity.orderItemInfoEntity;
        QOrderItemFeeEntity qof = QOrderItemFeeEntity.orderItemFeeEntity;
        var tuple = query.select(qoi, qof).from(qoi).leftJoin(qof).on(qoi.orderItemId.eq(qof.orderItemId)).where(qoi.orderId.in(List.of("202212275427151578112", "202212231348875927552"))).fetch();
        System.out.println(111);
    }

}
