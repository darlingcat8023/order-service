package com.star.enterprise.order.refund.calculator;

import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.adapter.TargetUserDesAdapter;
import com.star.enterprise.order.core.data.es.entity.SearchedTarget;
import com.star.enterprise.order.refund.data.es.entity.OrderRefundSearchInfoEntity;
import com.star.enterprise.order.refund.data.jpa.OrderRefundTargetCacheRepository;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundTargetCacheEntity;
import com.star.enterprise.order.refund.model.OrderRefundInfo;
import com.star.enterprise.order.remote.student.RemoteStudentService;
import com.star.enterprise.order.remote.student.request.RemoteStudentInfoRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Component
@AllArgsConstructor
public class RefundOrderTargetProcessor implements OrderRefundCalculator {

    private final OrderRefundTargetCacheRepository targetCacheRepository;

    private final RemoteStudentService remoteStudentService;

    @Override
    public void preCalculate(TargetUser target, OrderRefundInfo refundInfo, RefundAccumulateHolder holder) {
        var response = this.remoteStudentService.getUserInfoByTargetId(new RemoteStudentInfoRequest(target.targetId(), target.campus()));
        target.context().setUserName(response.userName()).setUserMobile(response.userMobile()).setUserNumber(response.userNumber()).setCampusName(response.departmentName())
                .setChannelId(response.channelId()).setChannelName(response.channelName()).setSubChannelId(response.subChannelId()).setSubChannelName(response.subChannelName());
    }

    @Override
    public void postCalculate(String refundOrderId, TargetUser target, OrderRefundInfo refundInfo, RefundAccumulateHolder holder) {
        var ctx = target.context();
        var entity = new OrderRefundTargetCacheEntity().setRefundOrderId(refundOrderId).setTargetId(target.targetId()).setTargetCampusId(target.campus()).setTargetCampusName(ctx.getCampusName())
                .setTargetStatus(target.executorStatus()).setTargetName(ctx.getUserName()).setTargetMobile(ctx.getUserMobile()).setTargetNumber(ctx.getUserNumber())
                .setChannelId(ctx.getChannelId()).setChannelName(ctx.getChannelName()).setSubChannelId(ctx.getSubChannelId()).setSubChannelName(ctx.getSubChannelName())
                .setUserToast(Jackson.writeString(new TargetUserDesAdapter(target)));
        this.targetCacheRepository.saveAndFlush(entity);
    }

    @Override
    public void preAsyncElastic(String refundOrderId, OrderRefundSearchInfoEntity entity, TargetUser target) {
        var st = new SearchedTarget().setTargetId(target.targetId()).setTargetMobile(target.context().getUserMobile())
                .setTargetNumber(target.context().getUserNumber()).setTargetName(target.context().getUserName())
                .setTargetCampus(target.campus()).setTargetCampusName(target.context().getCampusName())
                .setChannelId(target.context().getChannelId()).setSubChannelId(target.context().getSubChannelId());
        entity.setTarget(st);
        OrderRefundCalculator.super.preAsyncElastic(refundOrderId, entity, target);
    }
}
