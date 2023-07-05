package com.star.enterprise.order.refund.service;

import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.refund.data.es.entity.OrderRefundSearchInfoEntity;
import com.star.enterprise.order.refund.data.jpa.OrderRefundAdditionalInfoRepository;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundAdditionInfoEntity;
import com.star.enterprise.order.refund.model.RefundExtendInfo;
import com.star.enterprise.order.refund.model.trans.OrderRefundAdditionalSummaryTransObject;
import com.star.enterprise.order.refund.model.trans.OrderRefundSummaryTransObject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Service
@AllArgsConstructor
public class OrderRefundAdditionalService implements OrderRefundAsyncService {

    private final OrderRefundAdditionalInfoRepository additionalInfoRepository;

    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void saveAdditionalInfo(String refundInfoId, RefundExtendInfo refundExtendInfo) {
        if (refundExtendInfo == null) {
            return;
        }
        this.additionalInfoRepository.deleteByRefundOrderId(refundInfoId);
        var entity = new OrderRefundAdditionInfoEntity().setRefundOrderId(refundInfoId).setRefundMethod(refundExtendInfo.refundMethod())
                .setBankAccount(refundExtendInfo.bankAccount()).setBankId(refundExtendInfo.bankId()).setBankName(refundExtendInfo.bankName())
                .setCardNumber(refundExtendInfo.cardNumber()).setRemark(refundExtendInfo.remark()).setRefundReason(refundExtendInfo.refundReason())
                .setFile(Jackson.writeString(refundExtendInfo.files()));
        this.additionalInfoRepository.saveAndFlush(entity);
    }

    @Override
    public void asyncElastic(String refundOrderId, TargetUser target, OrderRefundSearchInfoEntity entity) {

    }

    @Override
    public void consume(Map<String, OrderRefundSummaryTransObject> objects) {
        this.additionalInfoRepository.findByRefundOrderIdIn(objects.keySet()).forEach(entity -> objects.get(entity.getRefundOrderId()).setAdditional(new OrderRefundAdditionalSummaryTransObject(entity)));
    }

}
