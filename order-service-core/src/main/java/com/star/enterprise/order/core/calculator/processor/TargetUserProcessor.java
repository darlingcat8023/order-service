package com.star.enterprise.order.core.calculator.processor;

import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.adapter.TargetUserDesAdapter;
import com.star.enterprise.order.core.calculator.CalculateProcessor;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.data.es.entity.SearchedTarget;
import com.star.enterprise.order.core.data.jpa.OrderTargetCacheRepository;
import com.star.enterprise.order.core.data.jpa.entity.OrderTargetCacheEntity;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import com.star.enterprise.order.remote.student.RemoteStudentService;
import com.star.enterprise.order.remote.student.request.RemoteStudentInfoRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xiaowenrou
 * @date 2022/11/1
 */
@Component
@AllArgsConstructor
public class TargetUserProcessor implements CalculateProcessor {

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    private final RemoteStudentService remoteStudentService;

    private final OrderTargetCacheRepository targetCacheRepository;

    @Override
    public void preCalculate(OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        var response = this.remoteStudentService.getUserInfoByTargetId(new RemoteStudentInfoRequest(target.targetId(), target.campus()));
        target.context().setUserName(response.userName()).setUserMobile(response.userMobile()).setUserNumber(response.userNumber()).setCampusName(response.departmentName())
                .setChannelId(response.channelId()).setChannelName(response.channelName()).setSubChannelId(response.subChannelId()).setSubChannelName(response.subChannelName());
        chain.preCalculate(orderDetail, chain, holder, target);
    }

    @Override
    public void postCalculate(String orderId, OrderFeeDetail orderDetail, CalculateProcessor chain, DelegatingAccumulateHolder holder, TargetUser target) {
        this.targetCacheRepository.findByOrderId(orderId).orElseGet(() -> {
            var ctx = target.context();
            var entity = new OrderTargetCacheEntity().setOrderId(orderId).setTargetId(target.targetId()).setTargetCampusId(target.campus()).setTargetCampusName(ctx.getCampusName())
                    .setTargetStatus(target.executorStatus()).setTargetName(ctx.getUserName()).setTargetMobile(ctx.getUserMobile()).setTargetNumber(ctx.getUserNumber())
                    .setChannelId(ctx.getChannelId()).setChannelName(ctx.getChannelName()).setSubChannelId(ctx.getSubChannelId()).setSubChannelName(ctx.getSubChannelName())
                    .setUserToast(Jackson.writeString(new TargetUserDesAdapter(target)));
            return this.targetCacheRepository.saveAndFlush(entity);
        });
        chain.postCalculate(orderId, orderDetail, chain, holder, target);
    }

    @Override
    public void preAsyncElastic(String orderId, OrderSearchInfoEntity entity, TargetUser target) {
        var st = new SearchedTarget().setTargetId(target.targetId()).setTargetMobile(target.context().getUserMobile())
                .setTargetNumber(target.context().getUserNumber()).setTargetName(target.context().getUserName())
                .setTargetCampus(target.campus()).setTargetCampusName(target.context().getCampusName())
                .setChannelId(target.context().getChannelId()).setSubChannelId(target.context().getSubChannelId());
        entity.setTarget(st);
        CalculateProcessor.super.preAsyncElastic(orderId, entity, target);
    }

    @Override
    public void postConsumerElastic(Map<String, OrderSummaryTransObject> objects) {
        CalculateProcessor.super.postConsumerElastic(objects);
    }

}
