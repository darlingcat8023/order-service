package com.star.enterprise.order.core.model.trans;

import com.fasterxml.jackson.core.type.TypeReference;
import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.core.adapter.MatchResultDesAdapter;
import com.star.enterprise.order.core.data.jpa.entity.OrderItemFeeEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author xiaowenrou
 * @date 2022/10/27
 */
@Data
@Accessors(chain = true)
public class OrderItemFeeSummaryTransObject {

    /**
     * 购买数量
     */
    private Integer number;

    /**
     * 原单价
     */
    private BigDecimal originSinglePrice;

    /**
     * 原总价
     */
    private BigDecimal originTotalPrice;

    /**
     * 优惠方案
     */
    private OrderDiscountPlanTransObject discountPlanObject;

    /**
     * 折后单价
     */
    private BigDecimal afterDiscountSinglePrice;

    /**
     * 折后总价
     */
    private BigDecimal afterDiscountTotalPrice;

    /**
     * 使用直减金额
     */
    private BigDecimal useDirect;

    /**
     * 使用零钱包的金额
     */
    private BigDecimal useWallet;

    /**
     * 应收金额
     */
    private BigDecimal dueCollectPrice;

    /**
     * 应收单价
     */
    private BigDecimal dueCollectSinglePrice;

    /**
     * 收费类型
     */
    private String chargeCategory;

    /**
     * 收费类型命中的规则id
     */
    private String chargeItemId;

    /**
     * 所有命中的收费类型
     */
    private List<MatchResult> matchedChargeCategory;

    public OrderItemFeeSummaryTransObject(OrderItemFeeEntity entity) {
        BeanUtils.copyProperties(entity, this);
        var type = new TypeReference<List<MatchResultDesAdapter>>(){};
        var list = Optional.ofNullable(entity.getMatchedChargeCategory()).map(matched -> Jackson.read(matched, type)).orElseGet(List::of);
        this.matchedChargeCategory = new ArrayList<>(list);
    }

}
