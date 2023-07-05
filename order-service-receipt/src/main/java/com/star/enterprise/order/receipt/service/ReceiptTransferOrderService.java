package com.star.enterprise.order.receipt.service;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.adapter.TargetUserDesAdapter;
import com.star.enterprise.order.core.data.es.entity.SearchedOperator;
import com.star.enterprise.order.core.model.EmployeeUser;
import com.star.enterprise.order.receipt.constants.ReceiptActionEnum;
import com.star.enterprise.order.receipt.constants.ReceiptStatusEnum;
import com.star.enterprise.order.receipt.data.es.entity.ReceiptSearchInfoEntity;
import com.star.enterprise.order.receipt.data.es.entity.SearchedAdditionalInfo;
import com.star.enterprise.order.receipt.data.es.entity.SearchedAggOrderItemInfo;
import com.star.enterprise.order.receipt.data.jpa.ReceiptTransferOrderRepository;
import com.star.enterprise.order.receipt.data.jpa.entity.ReceiptTransferOrderEntity;
import com.star.enterprise.order.receipt.model.ReceiptContext;
import com.star.enterprise.order.receipt.strategy.ReceiptNoStrategy;
import com.star.enterprise.order.transfer.model.TransferExtendInfo;
import com.star.enterprise.order.transfer.service.OrderTransferLifecycleService;
import com.star.enterprise.order.transfer.service.OrderTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.star.enterprise.order.base.exception.RestCode.RECEIPT_ITEM_NOT_FOUND;
import static com.star.enterprise.order.receipt.constants.ReceiptStatusEnum.CREATED;
import static com.star.enterprise.order.receipt.constants.ReceiptStatusEnum.DISCARD;
import static com.star.enterprise.order.receipt.constants.ReceiptTypeEnum.TRANSFER_ORDER;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * @author xiaowenrou
 * @date 2023/3/13
 */
@Service
@RequiredArgsConstructor
public class ReceiptTransferOrderService implements ReceiptService<TransferExtendInfo> {

    private final ApplicationContext applicationContext;

    private final ReceiptTransferOrderRepository receiptTransferOrderRepository;

    private final ReceiptNoStrategy receiptNoStrategy;

    private final ReceiptLogService logService;

    private final ReceiptSequenceService sequenceService;

    private final ReceiptSearchService searchService;

    private final OrderTransferService transferService;

    private final OrderTransferLifecycleService lifecycleService;

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public Receipt createReceipt(String orderId, TargetUser target, ReceiptContext context) {
        var orderInfo = this.transferService.findTransferOrderInfo(orderId).orElseThrow();
        Function<String, SearchedOperator> function = str -> Jackson.read(str, SearchedOperator.class);
        var create = Optional.ofNullable(orderInfo.getOperator()).filter(StringUtils::hasText)
                .map(function).orElseGet(SearchedOperator::defaultOperator);
        var ent = this.receiptTransferOrderRepository.findByTransferOrderId(orderId).orElseGet(() -> {
            var no = this.receiptNoStrategy.generateReceiptNo(target, orderId, TRANSFER_ORDER);
            var e = new ReceiptTransferOrderEntity().setTransferOrderId(orderId).setReceiptStatus(CREATED.value()).setReceiptNo(no)
                    .setTargetId(target.targetId()).setTarget(target).setPrint(0);
            this.logService.addReceiptLog(e, create, ReceiptActionEnum.CREATE);
            this.sequenceService.addReceiptSequence(e, target);
            return e;
        });
        context.getOperators().add(create.employeeName());
        ent.setTargetToast(Jackson.writeString(new TargetUserDesAdapter(target))).setContext(context);
        return this.receiptTransferOrderRepository.saveAndFlush(ent);
    }

    @Override
    public void modifyReceipt(String receiptNo, TransferExtendInfo extendInfo, Map<String, MatchResult> categories, EmployeeUser operator) {
        var receipt = this.receiptTransferOrderRepository.findByReceiptNo(receiptNo).orElseThrow(() -> new BusinessWarnException(RECEIPT_ITEM_NOT_FOUND, "error.receipt.notFound"));
        Runnable run = () -> {
            this.lifecycleService.modifyExistTransferOrder(receipt.orderId(), extendInfo);
            var operators = this.logService.distinctOperator(receiptNo);
            this.searchService.refresh(receipt, operators);
        };
        ReceiptStatusEnum.of(receipt.getReceiptStatus()).getHandler(this.applicationContext).processReceiptModify(this, receipt, operator, run);
    }

    @Override
    public void discardReceipt(String receiptNo, EmployeeUser operator) {
        var receipt = this.receiptTransferOrderRepository.findByReceiptNo(receiptNo).orElseThrow(() -> new BusinessWarnException(RECEIPT_ITEM_NOT_FOUND, "error.receipt.notFound"));
        Runnable runnable = () -> {
            this.lifecycleService.cancelTransferOrder(new AtomicReference<>(receipt.orderId()));
            var operators = this.logService.distinctOperator(receiptNo);
            receipt.setReceiptStatus(DISCARD.value()).setContext(new ReceiptContext().setOperators(operators));
            this.receiptTransferOrderRepository.saveAndFlush(receipt);
        };
        ReceiptStatusEnum.of(receipt.getReceiptStatus()).getHandler(this.applicationContext).processReceiptDiscard(this, receipt, operator, runnable);
    }

    @Override
    public void buildSyncData(Receipt receipt, ReceiptSearchInfoEntity entity) {
        var orderSummary = this.transferService.getSummaryObject(receipt.orderId());
        entity.setOrderId(orderSummary.getTransferOrderId()).setCreatedAt(orderSummary.getLastModifyDate());
        var items = orderSummary.getItems().stream().map(item -> new SearchedAggOrderItemInfo().setOrderItemId(item.getTransferOrderItemId()).setOriginReceiptNo(item.getOrderPaidReceiptNo())
                .setOriginOrderId(item.getOrderId()).setOriginOrderItemId(item.getOrderItemId()).setBusinessId(item.getBusinessId()).setBusinessType(item.getBusinessType()).
                setProductName(item.getBusinessName()).setSpecId(item.getSpecId()).setSpecName(item.getSpecName()).setNumber(item.getNumber()).setApportion(item.getApportion()).
                setRefundNumber(item.getItemFee().getTransferNumber()).setRefundApportion(item.getItemFee().getTransferApportion())).collect(Collectors.toList());
        entity.setItems(items).setDueTransferPrice(orderSummary.getTransferFee().getDueTransferPrice()).setFinalTransferPrice(orderSummary.getTransferFee().getFinalTransferPrice());
        var add = orderSummary.getAdditional();
        var additional = new SearchedAdditionalInfo().setInnerRemark(add.getRemark()).setReason(add.getTransferReason());
        entity.setAdditional(additional);
    }

}
