package com.star.enterprise.order.charge.matcher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaowenrou
 * @date 2022/9/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargeMatchResult implements MatchResult {

    private String chargeItemId;

    private String chargeCategory;

    @Override
    public String chargeItemId() {
        return this.chargeItemId;
    }

    @Override
    public String chargeCategory() {
        return this.chargeCategory;
    }

}
