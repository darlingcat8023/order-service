package com.star.enterprise.order.remote.course.response;

/**
 * @author xiaowenrou
 * @date 2022/11/4
 */
public record SpecPriceRecord(

        String specName,

        Boolean stairSpec,

        StandardSpecRecord standardSpecRecord,

        StairSpecRecord stairSpecRecord

) {

    /**
     * 空对象
     * @return
     */
    public static SpecPriceRecord empty() {
        return new SpecPriceRecord(null, null, null, null);
    }


}
