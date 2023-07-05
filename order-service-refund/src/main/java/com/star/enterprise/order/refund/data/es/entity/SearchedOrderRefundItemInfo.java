package com.star.enterprise.order.refund.data.es.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

/**
 * @author xiaowenrou
 * @date 2022/11/1
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchedOrderRefundItemInfo {

    @Field(type = Keyword)
    private String receiptNo;

    @Field(type = Keyword)
    private String orderId;

    @Field(type = Keyword)
    private String orderItemId;

    @Field(type = Keyword)
    private String refundOrderItemId;

    @Field(type = Keyword)
    private String businessType;

    @Field(type = Keyword)
    private String businessId;

    private String productName;

    private String specName;

}
