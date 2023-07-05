package com.star.enterprise.order.receipt.data.es.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Field;

import java.math.BigDecimal;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

/**
 * @author xiaowenrou
 * @date 2023/3/9
 */
@Data
@Accessors(chain = true)
public class SearchedAggOrderItemInfo {

    /**
     * 订单项id
     */
    @Field(type = Keyword)
    private String orderItemId;

    /**
     * 原收据号
     */
    @Field(type = Keyword)
    private String originReceiptNo;

    /**
     * 原订单号
     */
    private String originOrderId;

    /**
     * 原订单itemId
     */
    private String originOrderItemId;

    /**
     * 业务id
     */
    private String businessId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 产品名
     */
    private String productName;

    /**
     * 规格id
     */
    private String specId;

    /**
     * 规格名
     */
    private String specName;

    /**
     * 购买数量
     */
    private BigDecimal number;

    /**
     * 退费数量
     */
    private BigDecimal refundNumber;

    /**
     * 结转数量
     */
    private BigDecimal transferNumber;

    /**
     * 附加数量
     */
    private BigDecimal apportion;

    /**
     * 数量
     */
    private BigDecimal refundApportion;

    /**
     * 结转数量
     */
    private BigDecimal transferApportion;

    /**
     * 收费类型
     */
    @Field(type = Keyword)
    private String chargeCategory;

    /**
     * 优惠方案id
     */
    @Field(type = Keyword)
    private String discountPlanId;

    /**
     * 优惠方案名
     */
    private String discountPlanName;

}
