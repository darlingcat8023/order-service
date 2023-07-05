package com.star.enterprise.order.core.handler.business;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.OrderItemFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.data.jpa.OrderItemLeftRepository;
import com.star.enterprise.order.core.data.jpa.entity.OrderItemLeftEntity;
import com.star.enterprise.order.remote.course.RemoteCourseService;
import com.star.enterprise.order.remote.course.response.SpecPriceRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/11/4
 */
@Slf4j
@Component
@AllArgsConstructor
public class ArticleBusinessHandler extends BusinessTypeHandler {

    private final RemoteCourseService remoteCourseService;

    private final OrderItemLeftRepository orderItemLeftRepository;

    @Override
    public void processItemBusinessInfo(TargetUser targetUser, OrderItemFeeDetail item, DelegatingAccumulateHolder holder) {
        var response = this.remoteCourseService.getArticlePriceById(item.businessId(), targetUser.targetId());
        item.context().setProductName(response.productName()).setSpecRecord(SpecPriceRecord.empty());
        var opt = item.operator();
        opt.setOriginalSinglePrice(response.originalPrice());
        opt.setOriginalTotalPrice(opt.getOriginalSinglePrice().multiply(new BigDecimal(item.number())));
        super.processItemBusinessInfo(targetUser, item, holder);
    }

    @Override
    public void processItemAfterSaved(String orderId, TargetUser targetUser, OrderItemFeeDetail item, DelegatingAccumulateHolder holder) {
        var orderItemId = item.context().getOrderItemId();
        var ent = new OrderItemLeftEntity().setOrderItemId(orderItemId).setBusinessId(item.businessId()).setBusinessType(item.businessType())
                .setNumberLeft(new BigDecimal(item.number())).setApportionLeft(new BigDecimal(item.apportion()));
        this.orderItemLeftRepository.saveAndFlush(ent);
        super.processItemAfterSaved(orderId, targetUser, item, holder);
    }

}
