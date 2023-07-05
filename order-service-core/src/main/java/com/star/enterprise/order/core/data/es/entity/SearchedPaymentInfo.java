package com.star.enterprise.order.core.data.es.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Field;

import java.math.BigDecimal;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

/**
 * @author xiaowenrou
 * @date 2022/11/1
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class SearchedPaymentInfo {

    @Field(type = Keyword)
    private String paymentMethod;

    @Field(type = Keyword)
    private String paymentAccount;

    private BigDecimal paymentAmount;

}
