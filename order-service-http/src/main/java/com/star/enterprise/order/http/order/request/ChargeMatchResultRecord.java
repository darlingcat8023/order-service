package com.star.enterprise.order.http.order.request;

import com.star.enterprise.order.charge.matcher.MatchResult;

/**
 * @author xiaowenrou
 * @date 2023/1/5
 */
public record ChargeMatchResultRecord(

        String chargeItemId,

        String chargeCategory

) implements MatchResult {}
