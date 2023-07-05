package com.star.enterprise.order.core.service;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.base.exception.RestCode;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.holder.AccumulateHolder;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.model.OrderCollectInfo;
import com.star.enterprise.order.core.model.OrderExtendInfo;
import com.star.enterprise.order.remote.system.RemoteSystemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * 订单锁帐周期服务
 * @author xiaowenrou
 * @date 2023/4/4
 */
@Service
@AllArgsConstructor
public class OrderLockService implements OrderVerifyService {

    private final RemoteSystemService systemService;

    @Override
    public void beforeOrderPaid(String orderId, TargetUser target, OrderCollectInfo info, AccumulateHolder holder) {
        this.checkLockRange(target, info.payload().businessDate(), () -> new BusinessWarnException(RestCode.CORE_OPERATE_NOT_ALLOW, "error.order.lockedDate"));
        OrderVerifyService.super.beforeOrderPaid(orderId, target, info, holder);
    }

    @Override
    public void beforeExistOrderModify(String orderId, TargetUser targetUser, OrderExtendInfo extendInfo, OrderInfoEntity entity) {
        this.checkLockRange(targetUser, entity.getCompletedDate(), () -> new BusinessWarnException(RestCode.CORE_OPERATE_NOT_ALLOW, "error.order.lockedOrder"));
        OrderVerifyService.super.beforeExistOrderModify(orderId, targetUser, extendInfo, entity);
    }

    @Override
    public void beforeOrderRollback(String orderId, TargetUser target, OrderInfoEntity entity) {
        this.checkLockRange(target, entity.getCompletedDate(), () -> new BusinessWarnException(RestCode.CORE_OPERATE_NOT_ALLOW, "error.order.lockedOrder"));
        OrderVerifyService.super.beforeOrderRollback(orderId, target, entity);
    }

    private void checkLockRange(TargetUser target, LocalDateTime baseTime, Supplier<BusinessWarnException> exceptionSupplier) {
        if (this.systemService.checkLockRange(target.campus(), baseTime).data()) {
            throw exceptionSupplier.get();
        }
    }

}
