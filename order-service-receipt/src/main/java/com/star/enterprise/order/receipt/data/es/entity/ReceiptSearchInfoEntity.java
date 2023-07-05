package com.star.enterprise.order.receipt.data.es.entity;

import com.star.enterprise.order.core.data.es.entity.SearchedPaymentInfo;
import com.star.enterprise.order.core.data.es.entity.SearchedTarget;
import com.star.enterprise.order.core.model.PerformanceUser;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.Id;
import java.lang.Integer;
import java.lang.Object;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.data.elasticsearch.annotations.Dynamic.TRUE;
import static org.springframework.data.elasticsearch.annotations.FieldType.Object;
import static org.springframework.data.elasticsearch.annotations.FieldType.*;

/**
 * @author xiaowenrou
 * @date 2023/3/8
 */
@Data
@Accessors(chain = true)
@Document(indexName = "tg-receipt", dynamic = TRUE)
public class ReceiptSearchInfoEntity {

    @Id
    private String id;

    /**
     * 收据类型
     */
    @Field(type = Keyword)
    private String receiptType;

    /**
     * 收据号
     */
    @Field(type = Keyword)
    private String receiptNo;

    /**
     * 订单号
     */
    @Field(type = Keyword)
    private String orderId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 收据状态
     */
    private String receiptStatus;

    /**
     * 是否打印
     */
    private Integer print;

    /**
     * 操作人
     */
    @Field(type = Text)
    private Set<String> operators;

    /**
     * 目标用户
     */
    @Field(type = Object)
    private SearchedTarget target;

    /**
     * 订单商品搜索
     */
    @Field(type = Nested)
    private List<SearchedAggOrderItemInfo> items;

    private BigDecimal useDirectDiscount;

    private BigDecimal useWallet;

    @Field(type = Nested)
    private List<SearchedCouponInfo> coupons;

    private BigDecimal dueCollectPrice;

    private BigDecimal finalCollectPrice;

    private BigDecimal dueRefundPrice;

    private BigDecimal finalRefundPrice;

    private BigDecimal dueTransferPrice;

    private BigDecimal finalTransferPrice;

    /**
     * 支付信息
     */
    @Field(type = Nested)
    private List<SearchedPaymentInfo> payments;

    /**
     * 附加信息
     */
    @Field(type = Object)
    private SearchedAdditionalInfo additional;

    /**
     * 业绩归属人
     */
    @Field(type = Nested)
    private List<PerformanceUser> performanceUsers;

    /**
     * 扩展信息
     */
    @Field(type = Object)
    private Map<String, Object> extendInfo = new HashMap<>();

}
