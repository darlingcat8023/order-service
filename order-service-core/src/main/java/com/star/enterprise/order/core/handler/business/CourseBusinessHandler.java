package com.star.enterprise.order.core.handler.business;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.base.utils.ParameterizedTypeMapUtils;
import com.star.enterprise.order.charge.matcher.ChargeHook;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.charge.matcher.business.CourseChargeBusiness;
import com.star.enterprise.order.charge.matcher.business.CourseChargeBusinessMatcher;
import com.star.enterprise.order.charge.model.Fee;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.OrderItemFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.remote.callback.BusinessFeeRecord;
import com.star.enterprise.order.remote.callback.CoursePaidCallBackRequest;
import com.star.enterprise.order.remote.callback.ExpireStrategyRecord;
import com.star.enterprise.order.remote.callback.TargetRecord;
import com.star.enterprise.order.remote.course.RemoteCourseService;
import com.star.enterprise.order.remote.course.response.SpecPriceRecord;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.star.enterprise.order.base.exception.RestCode.CORE_ARGUMENT_NOT_VALID;

/**
 * 对于课程类型的业务处理器
 * @author xiaowenrou
 * @date 2022/11/4
 */
@Slf4j
@Component
@AllArgsConstructor
public class CourseBusinessHandler extends BusinessTypeHandler {

    private final RemoteCourseService remoteCourseService;

    private final CourseChargeBusinessMatcher matcher;

    private final RestOperations restOperations;

    @Override
    public void processItemBusinessInfo(TargetUser targetUser, OrderItemFeeDetail item, DelegatingAccumulateHolder holder) {
        var response = this.remoteCourseService.getCoursePriceById(item.businessId(), item.specId(), targetUser.campus());
        var ctx = item.context();
        var opt = item.operator();
        ctx.setProductName(response.productName()).setSpecRecord(Objects.requireNonNullElseGet(response.specPrice(), SpecPriceRecord::empty));
        if (StringUtils.hasText(item.specId())) {
            var spec = response.specPrice();
            if (spec.stairSpec()) {
                // 阶梯规格使用规格单价
                var stair = spec.stairSpecRecord();
                if (item.number() < stair.bindMinNumber() || item.number() > stair.bindMaxNumber()) {
                    throw new BusinessWarnException(CORE_ARGUMENT_NOT_VALID, "error.order.business.courseStairPriceNotValid", stair.bindMinNumber(), stair.bindMaxNumber());
                }
                opt.setOriginalSinglePrice(stair.bindSinglePrice());
            } else {
                // 标准规格使用规格总价
                var stand = spec.standardSpecRecord();
                if (!item.number().equals(stand.bindNumber())) {
                    throw new BusinessWarnException(CORE_ARGUMENT_NOT_VALID, "error.order.business.courseStandardPriceNotValid", stand.bindNumber());
                }
                opt.setOriginalSinglePrice(stand.bindSinglePrice());
                opt.setOriginalTotalPrice(stand.bindTotalPrice());
                super.processItemBusinessInfo(targetUser, item, holder);
                return;
            }
        } else {
            // 没有规格就使用标准单价
            opt.setOriginalSinglePrice(response.standardPrice());
        }
        opt.setOriginalTotalPrice(opt.getOriginalSinglePrice().multiply(new BigDecimal(item.number())));
        super.processItemBusinessInfo(targetUser, item, holder);
    }

    @Override
    public void processItemAfterSaved(String orderId, TargetUser targetUser, OrderItemFeeDetail item, DelegatingAccumulateHolder holder) {
        log.info("course callback itemId = {}", item.context().getOrderItemId());
        super.processItemAfterSaved(orderId, targetUser, item, holder);
        if (StringUtils.hasText(item.callbackAddress())) {
            holder.addDelayTask(order -> this.sendHttpCallback(order, targetUser, item));
        }
    }

    private void sendHttpCallback(String orderId, TargetUser targetUser, OrderItemFeeDetail item) {
        var extendInfo = item.extendInfo();
        var opt = item.operator();
        var ctx = item.context();
        var strategy = ParameterizedTypeMapUtils.getAttribute(extendInfo, "expireStrategy", String.class);
        var afterDays = ParameterizedTypeMapUtils.getAttribute(extendInfo, "afterDays", Integer.class);
        var startTime = ParameterizedTypeMapUtils.getAttribute(extendInfo, "startTime", String.class);
        var endTime = ParameterizedTypeMapUtils.getAttribute(extendInfo, "endTime", String.class);
        var expire = new ExpireStrategyRecord(strategy, afterDays, startTime, endTime);
        var target = new TargetRecord(targetUser.targetId(), targetUser.campus());
        var fee = new BusinessFeeRecord(opt.getOriginalSinglePrice(), opt.getOriginalTotalPrice(), opt.getDueCollectSinglePrice(), opt.getDueCollectPrice());
        var request = new CoursePaidCallBackRequest(orderId, ctx.getOrderItemId(), item.businessId(), ctx.getProductName(), new BigDecimal(item.number()), new BigDecimal(item.apportion()), fee, expire, target);
        this.restOperations.postForObject(item.callbackAddress(), request, Void.class);
    }

    @Override
    public List<MatchResult> calculateChargeMatchResult(TargetUser target, OrderItemFeeDetail item) {
        return this.matcher.matchSingle(target, new CourseChargeAdapter(item));
    }

    @Override
    public void modifyChargeCategory(String orderId, String itemId, TargetUser target, MatchResult source, MatchResult object) {
        super.modifyChargeCategory(orderId, itemId, target, source, object);
        Consumer<ChargeHook> consumer = hook -> hook.accept(orderId, itemId, source.chargeItemId());
        if (StringUtils.hasText(source.chargeItemId())) {
            this.matcher.getRollbackHooks(source.chargeItemId(), target).forEach(consumer);
        }
        if (StringUtils.hasText(object.chargeItemId())) {
            this.matcher.getExecutedHooks(object.chargeItemId(), target).forEach(consumer);
        }
    }

    @AllArgsConstructor
    public static class CourseChargeAdapter implements OrderItemFeeDetail, CourseChargeBusiness {

        @Delegate
        private final OrderItemFeeDetail delegate;

        @Override
        public String uniqueId() {
            return this.delegate.businessId();
        }

        @Override
        public Fee fee() {
            return this.delegate.operator();
        }

    }

}
