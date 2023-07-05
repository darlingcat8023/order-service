package com.star.enterprise.order.core.data.es.entity;

import com.star.enterprise.order.core.model.PerformanceUser;
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
@Document(indexName = "tg-order", dynamic = TRUE)
public class OrderSearchInfoEntity {

    protected static final String ORDER_ROUTING = "order";

    @Id
    private String id;

    @Field(type = Keyword)
    private String orderId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Field(type = Keyword)
    private String orderSource;

    @Field(type = Object)
    private SearchedOperator operator;

    @Field(type = Keyword)
    private String status;

    private LocalDateTime completedDate;

    private BigDecimal orderDueCollectPrice;

    @Field(type = Nested)
    private List<SearchedOrderItemInfo> items;

    @Field(type = Nested)
    private List<PerformanceUser> performanceUsers;

    @Field(type = Object)
    private SearchedTarget target;

    @Field(type = Nested)
    private List<SearchedPaymentInfo> payments;

    @Field(type = Object)
    private Map<String, String> additional = new HashMap<>(1 << 4);

}
