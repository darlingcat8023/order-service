package com.star.enterprise.order.charge.matcher.business;

import com.star.enterprise.order.charge.constants.ChargeCategoryEnum;
import com.star.enterprise.order.charge.matcher.ChargeHook;
import com.star.enterprise.order.charge.matcher.ChargeMatchResult;
import com.star.enterprise.order.charge.matcher.ChargeMatcher;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.charge.model.TargetUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.star.enterprise.order.charge.constants.ChargeCategoryEnum.*;

/**
 * @author xiaowenrou
 * @date 2023/4/19
 */
@Component
@RequiredArgsConstructor
public final class WalletChargeBusinessMatcher implements ChargeMatcher<WalletChargeBusiness> {

    @Override
    public List<MatchResult> matchSingle(TargetUser target, WalletChargeBusiness product) {
        return Stream.of(DEPOSIT, DEPOSIT_NEW, DEPOSIT_RENEW, DEPOSIT_OTHER).map(ChargeCategoryEnum::value)
                .map(value -> new ChargeMatchResult(null, value)).map(item -> (MatchResult)item).toList();
    }

    @Override
    public MultiValueMap<String, MatchResult> matchMulti(TargetUser target, Collection<WalletChargeBusiness> products) {
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
