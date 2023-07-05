package com.star.enterprise.order.remote.course.response;

import java.math.BigDecimal;

/**
 * @author xiaowenrou
 * @date 2022/11/4
 */
public record StairSpecRecord(

        Integer bindMinNumber,

        Integer bindMaxNumber,

        BigDecimal bindSinglePrice

) {}
