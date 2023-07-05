package com.star.enterprise.order.charge.matcher.processor;

import com.star.enterprise.order.charge.data.jpa.ChargePropertyLimitRepository;
import com.star.enterprise.order.charge.data.jpa.entity.ChargePropertyLimitEntity;
import com.star.enterprise.order.charge.data.jpa.entity.QChargePropertyLimitEntity;
import com.star.enterprise.order.charge.matcher.Business;
import com.star.enterprise.order.charge.matcher.ChargeHook;
import com.star.enterprise.order.charge.matcher.PropertyMatchProcessor;
import com.star.enterprise.order.charge.model.ChargeProperties;
import com.star.enterprise.order.charge.model.TargetUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static com.star.enterprise.order.charge.constants.rule.BaseChargeRuleSupport.LIMIT;

/**
 * @author xiaowenrou
 * @date 2022/9/22
 */
@Component
@RequiredArgsConstructor
public class LimitMatchProcessor<B extends Business> implements PropertyMatchProcessor<B> {

    private final String prop = LIMIT.getProperty();

    private final ChargePropertyLimitRepository propertyLimitRepository;

    @Override
    public boolean process(TargetUser target, B product, String chargeItemId, MultiValueMap<String, ChargeProperties> propertyMap) {
        if (propertyMap.containsKey(this.prop)) {
            var iterator = propertyMap.get(this.prop).iterator();
            var limit = Integer.parseInt(iterator.next().getStandardValue());
            var cpl = QChargePropertyLimitEntity.chargePropertyLimitEntity;
            if (this.propertyLimitRepository.count(cpl.targetId.eq(target.targetId()).and(cpl.campusId.eq(target.campus())).and(cpl.chargeItemId.eq(chargeItemId))) >= limit) {
                return false;
            }
            // 添加钩子函数在保存订单的时候执行回调
            product.fee().addHooks(this.hook(target, chargeItemId));
            return true;
        }
        return true;
    }

    @Override
    public Optional<ChargeHook> getExecuteHook(TargetUser target, String chargeItemId, MultiValueMap<String, ChargeProperties> propertyMap) {
        if (!propertyMap.containsKey(this.prop)) {
            return Optional.empty();
        }
        return Optional.of(this.hook(target, chargeItemId));
    }

    @Override
    public Optional<ChargeHook> getRollbackHook(TargetUser target, String chargeItemId, MultiValueMap<String, ChargeProperties> propertyMap) {
        if (!propertyMap.containsKey(this.prop)) {
            return Optional.empty();
        }
        ChargeHook hook = (orderId, itemId, chargeId) -> {
            var cpl = QChargePropertyLimitEntity.chargePropertyLimitEntity;
            if (chargeItemId.equals(chargeId)) {
                var exp = cpl.orderId.eq(orderId).and(cpl.targetId.eq(target.targetId())).and(cpl.campusId.eq(target.campus()));
                this.propertyLimitRepository.findOne(exp).ifPresent(this.propertyLimitRepository::delete);
            }
        };
        return Optional.of(hook);
    }

    private ChargeHook hook(TargetUser target, String chargeItemId) {
        var cpl = QChargePropertyLimitEntity.chargePropertyLimitEntity;
        return (orderId, itemId, chargeId) -> {
            if (chargeItemId.equals(chargeId)) {
                var exp = cpl.orderId.eq(orderId).and(cpl.targetId.eq(target.targetId())).and(cpl.campusId.eq(target.campus()));
                if (this.propertyLimitRepository.findOne(exp).isEmpty()) {
                    this.propertyLimitRepository.saveAndFlush(new ChargePropertyLimitEntity().setOrderItemId(itemId).setChargeItemId(chargeId).setOrderId(orderId).setCampusId(target.campus()).setTargetId(target.targetId()));
                }
            }
        };
    }

}
