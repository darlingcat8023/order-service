package com.star.enterprise.order.remote.callback;

/**
 * @author xiaowenrou
 * @date 2023/2/10
 */
public record ExpireStrategyRecord(

        /*
         * 过期策略
         */
        String expireStrategy,

        /*
         * 几天之后
         */
        Integer afterDays,

        /*
         * 开始时间
         */
        String startTime,

        /*
         * yyyy-MM-dd hh:mm:ss:SSS
         */
        String endTime

) {}
