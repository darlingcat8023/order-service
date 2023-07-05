package com.star.enterprise.order.http.order.response;

/**
 * @author xiaowenrou
 * @date 2022/10/28
 */
public record ChargeMatchResultResponseRecord(

        String chargeItemId,

        String chargeCategoryValue,

        String chargeCategoryDesc

) {}
