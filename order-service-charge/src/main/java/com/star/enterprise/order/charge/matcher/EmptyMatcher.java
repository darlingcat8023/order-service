package com.star.enterprise.order.charge.matcher;

import com.star.enterprise.order.charge.constants.ChargeCategoryEnum;
import com.star.enterprise.order.charge.model.TargetUser;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 空实现
 * @author xiaowenrou
 * @date 2022/9/22
 */
public final class EmptyMatcher implements ChargeMatcher<Business> {

    /**
     * 空实现默认全部匹配
     * @param product
     * @return
     */
    @Override
    public List<MatchResult> matchSingle(TargetUser target, Business product) {
        return Arrays.stream(ChargeCategoryEnum.values()).map(ChargeCategoryEnum::value)
                .map(value -> new ChargeMatchResult(null, value)).map(item -> (MatchResult)item).toList();
    }

    @Override
    public MultiValueMap<String, MatchResult> matchMulti(TargetUser target, Collection<Business> products) {
        var ret = new LinkedMultiValueMap<String, MatchResult>();
        products.forEach(product -> ret.addAll(product.uniqueId(), this.matchSingle(target, product)));
        return ret;
    }

    @Override
    public List<ChargeHook> getExecutedHooks(String chargeId, TargetUser target) {
        return List.of();
    }

    @Override
    public List<ChargeHook> getRollbackHooks(String chargeId, TargetUser target) {
        return List.of();
    }

}
