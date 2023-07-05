package com.star.enterprise.order.core.calculator.processor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.star.enterprise.order.base.utils.EnumSerializeUtils;
import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.constants.ChargeCategoryEnum;
import com.star.enterprise.order.charge.matcher.ChargeMatchResult;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculateProcessor;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.data.jpa.OrderItemFeeRepository;
import com.star.enterprise.order.core.handler.business.BusinessTypeHandler;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 计算费用类型
 * @author xiaowenrou
 * @date 2022/9/26
 */
@Slf4j
@Component
@AllArgsConstructor
public class ChargeCategoryProcessor implements CalculateProcessor {

    private final ApplicationContext applicationContext;

    private final OrderItemFeeRepository orderItemFeeRepository;

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    @Override
    public void preCalculate(OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        orderDetail.items().forEach(item -> {
            item.operator().addChargeCategory(item.context().getHandler().calculateChargeMatchResult(target, item));
        });
        chain.preCalculate(orderDetail, chain, holder, target);
    }

    @Override
    public void postCalculate(String orderId, OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        chain.postCalculate(orderId, orderDetail, chain, holder, target);
    }

    @Override
    public void postConsumerElastic(Map<String, OrderSummaryTransObject> objects) {
        objects.forEach((orderId, object) -> object.getItems().forEach(item -> {
            var itemFee = item.getItemFee();
            var list = itemFee.getMatchedChargeCategory().stream().map(MatchResultWrapper::new).map(x -> (MatchResult)x).toList();
            itemFee.setMatchedChargeCategory(list);
        }));
        CalculateProcessor.super.postConsumerElastic(objects);
    }

    public void processChargeCategoryModify(String orderId, TargetUser target, Map<String, MatchResult> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return;
        }
        this.orderItemFeeRepository.findByOrderId(orderId).forEach(item -> {
            if (!categories.containsKey(item.getOrderItemId())) {
                return;
            }
            var cate = categories.get(item.getOrderItemId());
            var source = new ChargeMatchResult(item.getChargeItemId(), item.getChargeCategory());
            if (source.equals(cate)) {
                return;
            }
            var handler = BusinessTypeHandler.createHandler(BusinessTypeEnum.of(item.getBusinessType()), this.applicationContext);
            handler.modifyChargeCategory(orderId, item.getOrderItemId(), target, source, cate);
            this.orderItemFeeRepository.save(item.setChargeCategory(cate.chargeCategory()).setChargeItemId(cate.chargeItemId()));
        });
    }

    /**
     * 这个是一个装饰器
     */
    @Data
    public static final class MatchResultWrapper implements MatchResult {

        private static Map<String, String> categoryMap = EnumSerializeUtils.toMap(ChargeCategoryEnum.values());

        @Delegate
        @JsonIgnore
        private final MatchResult result;

        private final String desc;

        public MatchResultWrapper(MatchResult result) {
            this.result = result;
            this.desc = categoryMap.get(result.chargeCategory());
        }

        public String getChargeItemId() {
            return this.result.chargeItemId();
        }

        public String getChargeCategory() {
            return this.result.chargeCategory();
        }

    }

}
