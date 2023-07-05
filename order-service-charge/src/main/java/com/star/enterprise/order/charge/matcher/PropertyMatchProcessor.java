package com.star.enterprise.order.charge.matcher;

import com.star.enterprise.order.charge.model.ChargeProperties;
import com.star.enterprise.order.charge.model.TargetUser;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

/**
 * 属性处理器
 * @author xiaowenrou
 * @date 2022/9/22
 */
public interface PropertyMatchProcessor<T> extends PriorityOrdered {

    /**
     * 默认的排序值
     * @return
     */
    @Override
    default int getOrder() {
        return 100;
    }

    /**
     * 处理具体的属性
     * @param target
     * @param product
     * @param propertyMap
     * @return
     */
    boolean process(TargetUser target, T product, String chargeItemId, MultiValueMap<String, ChargeProperties> propertyMap);

    /**
     * 获取钩子
     * @param target
     * @param propertyMap
     * @return
     */
    default Optional<ChargeHook> getExecuteHook(TargetUser target, String chargeItemId, MultiValueMap<String, ChargeProperties> propertyMap) {
        return Optional.empty();
    }

    /**
     * 获取回滚钩子
     * @param target
     * @param propertyMap
     * @return
     */
    default Optional<ChargeHook> getRollbackHook(TargetUser target, String chargeItemId, MultiValueMap<String, ChargeProperties> propertyMap) {
        return Optional.empty();
    }

}
