package com.star.enterprise.order.core.calculator.processor;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.CalculateProcessor;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.data.jpa.OrderCouponCommitRepository;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import com.star.enterprise.order.remote.coupon.RemoteCouponService;
import com.star.enterprise.order.remote.coupon.request.CouponLockRecord;
import com.star.enterprise.order.remote.coupon.request.CouponRecord;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

import static com.star.enterprise.order.base.exception.RestCode.CORE_REMOTE_FAIL;

/**
 * @author xiaowenrou
 * @date 2023/3/20
 */
@Component
@AllArgsConstructor
public class CouponExecuteProcessor implements CalculateProcessor {

    @Delegate(types = {PriorityOrdered.class})
    private final CouponProcessor delegate;

    private final RemoteCouponService couponService;

    private final OrderCouponCommitRepository commitRepository;

    @Override
    public void preCalculate(OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        this.delegate.preCalculate(orderDetail, chain, holder, target);
    }

    @Override
    public void postCalculate(String orderId, OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        this.delegate.postCalculate(orderId, orderDetail, chain, holder, target);
        var coupons = orderDetail.discount().couponDiscount();
        if (coupons == null || CollectionUtils.isEmpty(coupons)) {
            return;
        }
        var records = coupons.stream().map(coupon -> new CouponRecord(coupon.templateId(), coupon.couponCode(), coupon.order())).toList();
        var lockRes = this.couponService.lockCoupons(target.targetId(), target.campus(), new CouponLockRecord(orderId, records));
        if (!lockRes.message()) {
            throw new BusinessWarnException(CORE_REMOTE_FAIL, "error.order.couponLockFail");
        }
    }

    @Override
    public void postRollbackProcess(String orderId, TargetUser target, CalculateProcessor chain) {
        this.delegate.postRollbackProcess(orderId, target, chain);
    }

    @Override
    public void preAsyncElastic(String orderId, OrderSearchInfoEntity entity, TargetUser target) {
        this.delegate.preAsyncElastic(orderId, entity, target);
    }

    @Override
    public void postConsumerElastic(Map<String, OrderSummaryTransObject> objects) {
        this.delegate.postConsumerElastic(objects);
    }

}
