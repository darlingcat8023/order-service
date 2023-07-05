package com.star.enterprise.order.charge.matcher;

import com.star.enterprise.order.charge.model.TargetUser;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;

/**
 * 比较器
 * @author xiaowenrou
 * @date 2022/9/22
 */
public interface ChargeMatcher<T extends Business> {

    /**
     * single 比较器
     * @param target
     * @param product
     * @return
     */
    List<MatchResult> matchSingle(TargetUser target, T product);

    /**
     * multi 比较器
     * @param target
     * @param products
     * @return
     */
    MultiValueMap<String, MatchResult> matchMulti(TargetUser target, Collection<T> products);

    /**
     * 获取所有执行的钩子函数
     * @param chargeId
     * @param target
     * @return
     */
    List<ChargeHook> getExecutedHooks(String chargeId, TargetUser target);

    /**
     * 获取所有回滚的钩子函数
     * @param chargeId
     * @param target
     * @return
     */
    List<ChargeHook> getRollbackHooks(String chargeId, TargetUser target);

}
