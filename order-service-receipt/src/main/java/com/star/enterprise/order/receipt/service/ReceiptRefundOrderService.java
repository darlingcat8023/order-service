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
import com.star.enterprise.order.receipt.data.jpa.ReceiptRefundOrderRepository;
import com.star.enterprise.order.receipt.data.jpa.entity.ReceiptRefundOrderEntity;
import com.star.enterprise.order.receipt.model.ReceiptContext;
import com.star.enterprise.order.receipt.strategy.ReceiptNoStrategy;
import com.star.enterprise.order.refund.model.RefundExtendInfo;
import com.star.enterprise.order.refund.service.OrderRefundLifecycleService;
import com.star.enterprise.order.refund.service.OrderRefundService;
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
import static com.star.enterprise.order.receipt.constants.ReceiptTypeEnum.REFUND_ORDER;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * @author xiaowenrou
 * @date 2023/3/13
 */
@Service
@RequiredArgsConstructor
public class ReceiptRefundOrderService implements ReceiptService<RefundExtendInfo> {

    private final ApplicationContext applicationContext;

    private final ReceiptRefundOrderRepository receiptRefundOrderRepository;

    private final ReceiptNoStrategy receiptNoStrategy;

    private final ReceiptLogService logService;

    private final ReceiptSequenceService sequenceService;

    private final ReceiptSearchService searchService;

    private final OrderRefundService refundService;

    private final OrderRefundLifecycleService lifecycleService;

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public Receipt createReceipt(String orderId, TargetUser target, ReceiptContext context) {
        var orderInfo = this.refundService.findRefundOrderInfo(orderId).orElseThrow();
        Function<String, SearchedOperator> function = str -> Jackson.read(str, SearchedOperator.class);
        var create = Optional.ofNullable(orderInfo.getOperator()).filter(StringUtils::hasText)
                .map(function).orElseGet(SearchedOperator::defaultOperator);
        var ent = this.receiptRefundOrderRepository.findByRefundOrderId(orderId).orElseGet(() -> {
            var no = this.receiptNoStrategy.generateReceiptNo(target, orderId, REFUND_ORDER);
            var e = new ReceiptRefundOrderEntity().setRefundOrderId(orderId).setReceiptStatus(CREATED.value()).setReceiptNo(no)
                    .setTargetId(target.targetId()).setTarget(target).setPrint(0);
            this.logService.addReceiptLog(e, create, ReceiptActionEnum.CREATE);
            this.sequenceService.addReceiptSequence(e, target);
            return e;
        });
        context.getOperators().add(create.employeeName());
        ent.setTargetToast(Jackson.writeString(new TargetUserDesAdapter(target))).setContext(context);
        return this.receiptRefundOrderRepository.saveAndFlush(ent);
    }

    @Override
    public void modifyReceipt(String receiptNo, RefundExtendInfo extendInfo, Map<String, MatchResult> categories, EmployeeUser operator) {
        var receipt = this.receiptRefundOrderRepository.findByReceiptNo(receiptNo).orElseThrow(() -> new BusinessWarnException(RECEIPT_ITEM_NOT_FOUND, "error.receipt.notFound"));
        Runnable run = () -> {
            this.lifecycleService.modifyExistRefundOrder(receipt.orderId(), extendInfo);
            var operators = this.logService.distinctOperator(receiptNo);
            this.searchService.refresh(receipt, operators);
        };
        ReceiptStatusEnum.of(receipt.getReceiptStatus()).getHandler(this.applicationContext).processReceiptModify(this, receipt, operator, run);
    }

    @Override
    public void discardReceipt(String receiptNo, EmployeeUser operator) {
        var receipt = this.receiptRefundOrderRepository.findByReceiptNo(receiptNo).orElseThrow(() -> new BusinessWarnException(RECEIPT_ITEM_NOT_FOUND, "error.receipt.notFound"));
        Runnable runnable = () -> {
            this.lifecycleService.cancelRefundOrder(new AtomicReference<>(receipt.orderId()));
            var operators = this.logService.distinctOperator(receiptNo);
            receipt.setReceiptStatus(DISCARD.value()).setContext(new ReceiptContext().setOperators(operators));
            this.receiptRefundOrderRepository.saveAndFlush(receipt);
        };
        ReceiptStatusEnum.of(receipt.getReceiptStatus()).getHandler(this.applicationContext).processReceiptDiscard(this, receipt, operator, runnable);
    }

    @Override
    public void buildSyncData(Receipt receipt, ReceiptSearchInfoEntity entity) {
        var orderSummary = this.refundService.getSummaryObject(receipt.orderId());
        entity.setOrderId(orderSummary.getRefundOrderId()).setCreatedAt(orderSummary.getLastModifyDate());
        var items = orderSummary.getItems().stream().map(item -> new SearchedAggOrderItemInfo().setOrderItemId(item.getRefundOrderItemId()).setOriginReceiptNo(item.getOrderPaidReceiptNo())
                .setOriginOrderId(item.getOrderId()).setOriginOrderItemId(item.getOrderItemId()).setBusinessId(item.getBusinessId()).setBusinessType(item.getBusinessType()).
                setProductName(item.getBusinessName()).setSpecId(item.getSpecId()).setSpecName(item.getSpecName()).setNumber(item.getNumber()).setApportion(item.getApportion()).
                setRefundNumber(item.getItemFee().getRefundNumber()).setRefundApportion(item.getItemFee().getRefundApportion())).collect(Collectors.toList());
        entity.setItems(items).setDueRefundPrice(orderSummary.getRefundFee().getDueRefundPrice()).setFinalRefundPrice(orderSummary.getRefundFee().getFinalRefundPrice());
        var add = orderSummary.getAdditional();
        var additional = new SearchedAdditionalInfo().setInnerRemark(add.getRemark()).setReason(add.getRefundReason());
        entity.setAdditional(additional);
    }

}
