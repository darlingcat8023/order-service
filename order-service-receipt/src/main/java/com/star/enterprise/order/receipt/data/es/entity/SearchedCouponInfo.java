package com.star.enterprise.order.receipt.data.es.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

/**
 * @author xiaowenrou
 * @date 2023/3/14
 */
@Data
@Accessors(chain = true)
public class SearchedCouponInfo {

    @Field(type = Keyword)
    private String couponNo;

    private String couponName;

    @Field(type = Keyword)
    private String templateId;

}
