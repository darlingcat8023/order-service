package com.star.enterprise.order.transfer.service;

import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.transfer.data.es.entity.OrderTransferSearchInfoEntity;
import com.star.enterprise.order.transfer.data.jpa.OrderTransferAdditionalInfoRepository;
import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferAdditionInfoEntity;
import com.star.enterprise.order.transfer.model.TransferExtendInfo;
import com.star.enterprise.order.transfer.model.trans.OrderTransferAdditionalSummaryTransObject;
import com.star.enterprise.order.transfer.model.trans.OrderTransferSummaryTransObject;
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
public class OrderTransferAdditionalService implements OrderTransferAsyncService {

    private final OrderTransferAdditionalInfoRepository additionalInfoRepository;

    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void saveAdditionalInfo(String transferOrderId, TransferExtendInfo transferExtendInfo) {
        if (transferExtendInfo == null) {
            return;
        }
        this.additionalInfoRepository.deleteByTransferOrderId(transferOrderId);
        var entity = new OrderTransferAdditionInfoEntity().setTransferOrderId(transferOrderId).setRemark(transferExtendInfo.remark())
                .setTransferReason(transferExtendInfo.transferReason()).setFile(Jackson.writeString(transferExtendInfo.files()));
        this.additionalInfoRepository.saveAndFlush(entity);
    }

    @Override
    public void asyncElastic(String refundOrderId, TargetUser target, OrderTransferSearchInfoEntity entity) {}

    @Override
    public void consume(Map<String, OrderTransferSummaryTransObject> objects) {
        this.additionalInfoRepository.findByTransferOrderIdIn(objects.keySet()).forEach(entity -> objects.get(entity.getTransferOrderId()).setAdditional(new OrderTransferAdditionalSummaryTransObject(entity)));
    }

}
