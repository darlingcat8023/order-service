package com.star.enterprise.order.transfer.data.es.entity;

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
@Document(indexName = "tg-order-transfer", dynamic = TRUE)
public class OrderTransferSearchInfoEntity {

    protected static final String ORDER_REFUND_ROUTING = "order-transfer";

    @Id
    private String id;

    @Field(type = Keyword)
    private String transferOrderId;

    @Field(type = Keyword)
    private String approvalId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Field(type = Object)
    private SearchedOperator operator;

    @Field(type = Keyword)
    private String transferStatus;

    private BigDecimal dueTransferPrice;

    private BigDecimal finalTransferPrice;

    @Field(type = Nested)
    private List<SearchedOrderTransferItemInfo> items;

    @Field(type = Object)
    private SearchedTarget target;

    @Field(type = Object)
    private Map<String, String> additional = new HashMap<>(1 << 4);

}
