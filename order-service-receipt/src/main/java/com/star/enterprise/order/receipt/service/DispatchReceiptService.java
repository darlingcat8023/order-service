package com.star.enterprise.order.receipt.service;

import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.model.EmployeeUser;
import com.star.enterprise.order.receipt.constants.ReceiptActionEnum;
import com.star.enterprise.order.receipt.data.jpa.ReceiptLogRepository;
import com.star.enterprise.order.receipt.data.jpa.ReceiptSequenceRepository;
import com.star.enterprise.order.receipt.data.jpa.entity.QReceiptSequenceEntity;
import com.star.enterprise.order.receipt.data.jpa.entity.ReceiptLogEntity;
import com.star.enterprise.order.receipt.data.jpa.entity.ReceiptSequenceEntity;
import com.star.enterprise.order.receipt.model.ReceiptOperator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * 此类只用来分发到不同的收据服务
 * @author xiaowenrou
 * @date 2022/11/29
 */
@Service
@AllArgsConstructor
public class DispatchReceiptService implements ReceiptLogService, ReceiptSequenceService {

    private final ReceiptSequenceRepository receiptSequenceRepository;

    private final ReceiptLogRepository logRepository;


    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void addReceiptLog(Receipt receipt, EmployeeUser operator, ReceiptActionEnum action) {
        var insert = new ReceiptLogEntity().setReceiptNo(receipt.receiptNo()).setOrderId(receipt.orderId())
                .setAction(action.value()).setOperator(operator.employeeName()).setOperatorId(operator.employeeId()).setCreatedAt(LocalDateTime.now());
        this.logRepository.saveAndFlush(insert);
    }

    @Override
    public Set<String> distinctOperator(String receiptNo) {
        return this.logRepository.findAllByReceiptNo(receiptNo).stream().map(ReceiptOperator::getOperator).collect(Collectors.toSet());
    }

    @Override
    public List<ReceiptOperator> listOperators(String receiptNo) {
        return this.logRepository.findAllByReceiptNo(receiptNo);
    }

    @Override
    public void addReceiptSequence(Receipt receipt, TargetUser target) {
        var seq = new ReceiptSequenceEntity().setReceiptNo(receipt.receiptNo()).setOrderId(receipt.orderId())
                .setTargetId(target.targetId()).setCampus(target.campus()).setCreatedAt(LocalDateTime.now());
        this.receiptSequenceRepository.saveAndFlush(seq);
    }

    @Override
    public Optional<String> latestReceiptNo(TargetUser target) {
        var qrs = QReceiptSequenceEntity.receiptSequenceEntity;
        var order = Sort.by("createdAt").descending();
        return this.receiptSequenceRepository.findBy(qrs.targetId.eq(target.targetId()).and(qrs.campus.eq(target.campus())), fluent -> fluent.sortBy(order).first())
                .map(ReceiptSequenceEntity::getReceiptNo);
    }

    @Override
    public void deleteReceiptSequence(String receiptNo) {
        this.receiptSequenceRepository.deleteByReceiptNo(receiptNo);
    }

}
