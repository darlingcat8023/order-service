package com.star.enterprise.order.refund.data.es.entity;

import com.star.enterprise.order.core.data.es.entity.SearchedOperator;
import com.star.enterprise.order.core.data.es.entity.SearchedTarget;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.elasticsearch.annotations.Dynamic.TRUE;
import static org.springframework.data.elasticsearch.annotations.FieldType.Object;
import static org.springframework.data.elasticsearch.annotations.FieldType.*;

/**
 * @author xiaowenrou
 * @date 2022/10/31
 */
@Data
@Accessors(chain = true)
@Document(indexName = "tg-order-refund", dynamic = TRUE)
public class OrderRefundSearchInfoEntity {

    protected static final String ORDER_REFUND_ROUTING = "order-refund";

    @Id
    private String id;

    @Field(type = Keyword)
    private String refundOrderId;

    @Field(type = Keyword)
    private String approvalId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Field(type = Object)
    private SearchedOperator operator;

    @Field(type = Keyword)
    private String refundStatus;

    private BigDecimal dueRefundPrice;

    private BigDecimal finalRefundPrice;

    @Field(type = Nested)
    private List<SearchedOrderRefundItemInfo> items;

    @Field(type = Object)
    private SearchedTarget target;

    @Field(type = Object)
    private Map<String, String> additional = new HashMap<>(1 << 4);

}
