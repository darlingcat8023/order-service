package com.star.enterprise.order.receipt.data.es.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

/**
 * @author xiaowenrou
 * @date 2023/3/8
 */
@Data
public class SearcherReceiptOperator {

    @Field(type = Keyword)
    private String operatorId;

    private String operatorName;

    @Field(type = Keyword)
    private String action;

}
