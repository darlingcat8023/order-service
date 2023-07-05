package com.star.enterprise.order.core.calculator.processor;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculateProcessor;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_DOWN;
import static java.math.RoundingMode.HALF_UP;

/**
 * 使用直减优惠
 * @author xiaowenrou
 * &#064;date  2022/9/29
 */
@Slf4j
@Component
public class DirectDiscountProcessor implements CalculateProcessor {

    @Override
    public int getOrder() {
        return 1000;
    }


    @Override
    public void preCalculate(OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        var discount = orderDetail.discount();
        // 不使用直减跳过
        if (discount.useDirectDiscount().compareTo(ZERO) == 0) {
            chain.preCalculate(orderDetail, chain, holder, target);
            return;
        }
        var count = orderDetail.additionalItemCount();
        // 没有分摊直接跳过
        if (count < 1) {
            holder.subtractOrderDueCollectPrice(discount.useDirectDiscount());
            chain.preCalculate(orderDetail, chain, holder, target);
            return;
        }
        // 计算分摊总价
        var t = new BigDecimal(0);
        for (var item : orderDetail.items()) {
            if (item.additional()) {
                t = t.add(item.operator().getAfterDiscountTotalPrice());
            }
        }
        // 分摊总数
        var current = new BigDecimal(0);
        for (var item : orderDetail.items()) {
            var operator = item.operator();
            if (item.additional()) {
                BigDecimal p;
                if (--count > 0) {
                    p = discount.useDirectDiscount().multiply(operator.getAfterDiscountTotalPrice()).divide(t, HALF_UP);
                    current = current.add(p);
                } else {
                    // 最后一个直接减
                    p = discount.useDirectDiscount().subtract(current);
                }
                operator.setUseDirect(p).setDueCollectPrice(operator.getDueCollectPrice().subtract(p))
                        .setDueCollectSinglePrice(operator.getDueCollectPrice().divide(new BigDecimal(item.number()), HALF_DOWN));
                holder.subtractOrderDueCollectPrice(p);
            }
            if (count == 0) {
                break;
            }
        }
        chain.preCalculate(orderDetail, chain, holder, target);
    }

    @Override
    public void postCalculate(String orderId, OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        chain.postCalculate(orderId, orderDetail, chain, holder, target);
    }

}
