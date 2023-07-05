package com.star.enterprise.order.receipt.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.adapter.TargetUserDesAdapter;
import com.star.enterprise.order.core.data.es.entity.SearchedOperator;
import com.star.enterprise.order.core.data.es.entity.SearchedPaymentInfo;
import com.star.enterprise.order.core.model.EmployeeUser;
import com.star.enterprise.order.core.model.OrderExtendInfo;
import com.star.enterprise.order.core.service.OrderLifecycleService;
import com.star.enterprise.order.core.service.OrderService;
import com.star.enterprise.order.receipt.constants.ReceiptActionEnum;
import com.star.enterprise.order.receipt.constants.ReceiptStatusEnum;
import com.star.enterprise.order.receipt.data.es.entity.ReceiptSearchInfoEntity;
import com.star.enterprise.order.receipt.data.es.entity.SearchedAdditionalInfo;
import com.star.enterprise.order.receipt.data.es.entity.SearchedAggOrderItemInfo;
import com.star.enterprise.order.receipt.data.jpa.ReceiptOrderPaidRepository;
import com.star.enterprise.order.receipt.data.jpa.entity.QReceiptOrderPaidEntity;
import com.star.enterprise.order.receipt.data.jpa.entity.ReceiptOrderPaidEntity;
import com.star.enterprise.order.receipt.model.ReceiptContext;
import com.star.enterprise.order.receipt.strategy.ReceiptNoStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.star.enterprise.order.base.exception.RestCode.RECEIPT_ITEM_NOT_FOUND;
import static com.star.enterprise.order.receipt.constants.ReceiptStatusEnum.CREATED;
import static com.star.enterprise.order.receipt.constants.ReceiptStatusEnum.DISCARD;
import static com.star.enterprise.order.receipt.constants.ReceiptTypeEnum.ORDER_PAID;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * @author xiaowenrou
 * @date 2022/11/29
 */
@Service
@RequiredArgsConstructor
public class ReceiptOrderPaidService implements ReceiptService<OrderExtendInfo> {

    private final ApplicationContext applicationContext;

    private final ReceiptOrderPaidRepository receiptOrderPaidRepository;

    private final ReceiptNoStrategy receiptNoStrategy;

    private final ReceiptLogService logService;

    private final ReceiptSequenceService sequenceService;

    private final ReceiptSearchService searchService;

    private final OrderLifecycleService orderLifecycleService;

    private final OrderService orderService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public Receipt createReceipt(String orderId, TargetUser target, ReceiptContext context) {
        var orderInfo = this.orderService.findOrderInfo(orderId).orElseThrow();
        Function<String, SearchedOperator> function = str -> Jackson.read(str, SearchedOperator.class);
        var create = Optional.ofNullable(orderInfo.getOperator()).filter(StringUtils::hasText)
                .map(function).orElseGet(SearchedOperator::defaultOperator);
        var charge = Optional.ofNullable(orderInfo.getLastModifiedBy()).filter(StringUtils::hasText)
                .map(function).orElseGet(SearchedOperator::defaultOperator);
        var ent = this.receiptOrderPaidRepository.findByOrderId(orderId).orElseGet(() -> {
            var no = this.receiptNoStrategy.generateReceiptNo(target, orderId, ORDER_PAID);
            var e = new ReceiptOrderPaidEntity().setOrderId(orderId).setReceiptStatus(CREATED.value()).setReceiptNo(no)
                    .setTargetId(target.targetId()).setTarget(target).setPrint(0);
            this.logService.addReceiptLog(e, create, ReceiptActionEnum.CREATE);
            this.logService.addReceiptLog(e, charge, ReceiptActionEnum.CHARGE);
            this.sequenceService.addReceiptSequence(e, target);
            return e;
        });
        context.getOperators().add(charge.employeeName());
        context.getOperators().add(create.employeeName());
        ent.setTargetToast(Jackson.writeString(new TargetUserDesAdapter(target))).setContext(context);
        return this.receiptOrderPaidRepository.saveAndFlush(ent);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void modifyReceipt(String receiptNo, OrderExtendInfo extendInfo, Map<String, MatchResult> categories, EmployeeUser operator) {
        var receipt = this.receiptOrderPaidRepository.findByReceiptNo(receiptNo).orElseThrow(() -> new BusinessWarnException(RECEIPT_ITEM_NOT_FOUND, "error.receipt.notFound"));
        Runnable run = () -> {
            this.orderLifecycleService.modifyExistOrder(receipt.getOrderId(), extendInfo, categories);
            var operators = this.logService.distinctOperator(receiptNo);
            this.searchService.refresh(receipt, operators);
        };
        ReceiptStatusEnum.of(receipt.getReceiptStatus()).getHandler(this.applicationContext).processReceiptModify(this, receipt, operator, run);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = REQUIRED)
    public void discardReceipt(String receiptNo, EmployeeUser operator) {
        var receipt = this.receiptOrderPaidRepository.findByReceiptNo(receiptNo).orElseThrow(() -> new BusinessWarnException(RECEIPT_ITEM_NOT_FOUND, "error.receipt.notFound"));
        Runnable runnable = () -> {
            this.orderLifecycleService.cancelOrder(new AtomicReference<>(receipt.orderId()));
            var operators = this.logService.distinctOperator(receiptNo);
            receipt.setReceiptStatus(DISCARD.value()).setContext(new ReceiptContext().setOperators(operators));
            this.receiptOrderPaidRepository.saveAndFlush(receipt);
        };
        ReceiptStatusEnum.of(receipt.getReceiptStatus()).getHandler(this.applicationContext).processReceiptDiscard(this, receipt, operator, runnable);
    }

    @Override
    public void buildSyncData(Receipt receipt, ReceiptSearchInfoEntity entity) {
        var orderSummary = this.orderService.getOrderInfoSummaryObject(receipt.orderId());
        entity.setOrderId(orderSummary.getOrderId()).setCreatedAt(orderSummary.getCompletedDate());
        var items = orderSummary.getItems().stream().map(item -> new SearchedAggOrderItemInfo().setOrderItemId(item.getOrderItemId()).setBusinessId(item.getBusinessId()).setBusinessType(item.getBusinessType()).setProductName(item.getProductName())
                        .setSpecId(item.getSpecId()).setSpecName(item.getSpecName()).setNumber(new BigDecimal(item.getNumber())).setApportion(new BigDecimal(item.getApportion())).setChargeCategory(item.getItemFee().getChargeCategory())).toList();
        entity.setItems(items).setDueCollectPrice(orderSummary.getOrderFee().getOrderDueCollectPrice()).setFinalCollectPrice(orderSummary.getOrderFee().getOrderDueCollectPrice())
                .setUseDirectDiscount(orderSummary.getOrderFee().getUseDirect()).setUseWallet(orderSummary.getOrderFee().getUseWallet());
        var pay = orderSummary.getPayments().stream().map(p -> new SearchedPaymentInfo(p.getPaymentMethod(), p.getPaymentAccount(), p.getPaymentAmount())).toList();
        entity.setPayments(pay);
        var add = orderSummary.getAdditional();
        var additional = new SearchedAdditionalInfo().setInvoiceNo(add.getInvoiceNo()).setInnerRemark(add.getInnerRemark()).setOuterRemark(add.getOuterRemark());
        entity.setPerformanceUsers(add.getPerformanceUser());
        entity.setAdditional(additional);
    }

    public Map<String, String> queryReceiptNo(Set<String> orderIds) {
        var query = new JPAQuery<>(this.entityManager);
        var exp = QReceiptOrderPaidEntity.receiptOrderPaidEntity;
        return query.select(exp.receiptNo, exp.orderId).from(exp).where(exp.orderId.in(orderIds)).stream()
                .collect(Collectors.toMap(tuple -> tuple.get(exp.orderId), tuple -> Objects.requireNonNullElse(tuple.get(exp.receiptNo), ""), (a, b) -> a));
    }

}
