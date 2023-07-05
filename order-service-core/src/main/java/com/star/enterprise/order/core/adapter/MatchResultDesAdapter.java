package com.star.enterprise.order.core.adapter;

import com.star.enterprise.order.charge.matcher.MatchResult;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaowenrou
 * @date 2023/1/5
 */
@Data
@NoArgsConstructor
public class MatchResultDesAdapter implements MatchResult {

    private String chargeItemId;

    private String chargeCategory;

    public MatchResultDesAdapter(MatchResult matchResult) {
        this.chargeItemId = matchResult.chargeItemId();
        this.chargeCategory = matchResult.chargeCategory();
    }

    @Override
    public String chargeItemId() {
        return this.chargeItemId;
    }

    @Override
    public String chargeCategory() {
        return this.chargeCategory;
    }
}
