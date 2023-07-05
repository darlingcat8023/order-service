package com.star.enterprise.order.http.reciept;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.http.advice.security.SecurityAudit;
import com.star.enterprise.order.http.advice.security.WebSecurityContext;
import com.star.enterprise.order.http.order.Verify;
import com.star.enterprise.order.http.order.request.OrderModifyRequest;
import com.star.enterprise.order.http.order.request.RefundOrderModifyRequest;
import com.star.enterprise.order.http.order.request.TransferOrderModifyRequest;
import com.star.enterprise.order.receipt.constants.ReceiptTypeEnum;
import com.star.enterprise.order.receipt.model.ReceiptOperator;
import com.star.enterprise.order.receipt.model.ReceiptSearchPredicate;
import com.star.enterprise.order.receipt.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.star.enterprise.order.base.exception.RestCode.HTTP_SECURITY_DECLINE;

/**
 * @author xiaowenrou
 * @date 2022/12/7
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/enterprise/order/receipt")
public class ReceiptController {

    private final ApplicationContext applicationContext;

    private final ReceiptSearchService searchService;

    private final ReceiptLogService receiptLogService;

    /**
     * 收据类型字典
     * @param dictionaryService
     * @return
     */
    @GetMapping(value = "/dic/type")
    public Map<?, ?> receiptTypeMap(@Value(value = "#{receiptDictionaryService}") ReceiptDictionaryService dictionaryService) {
        return dictionaryService.buildReceiptTypeDictionary();
    }

    /**
     * 收据状态字典
     * @param dictionaryService
     * @return
     */
    @GetMapping(value = "/dic/status")
    public Map<?, ?> receiptStatusMap(@Value(value = "#{receiptDictionaryService}") ReceiptDictionaryService dictionaryService) {
        return dictionaryService.buildReceiptStatusDictionary();
    }

    /**
     * 收据日志字典
     * @param dictionaryService
     * @return
     */
    @GetMapping(value = "/dic/action")
    public Map<?, ?> receiptActionMap(@Value(value = "#{receiptDictionaryService}") ReceiptDictionaryService dictionaryService) {
        return dictionaryService.buildReceiptActionDictionary();
    }

    /**
     * 分页查询收据
     * @param predicate
     * @param rollId
     * @param pageable
     */
    @PostMapping(value = "/page")
    public Page<?> pageReceipt(@RequestBody ReceiptSearchPredicate predicate, @RequestParam(required = false) String rollId, Pageable pageable) {
        return this.searchService.searchReceipt(predicate, rollId, pageable);
    }

    /**
     * 获取收据操作人
     * @param receiptNo
     * @return
     */
    @GetMapping(value = "/operator")
    public List<ReceiptOperator> listOperators(@RequestParam(value = "receiptNo") String receiptNo) {
        return this.receiptLogService.listOperators(receiptNo);
    }

    /**
     * 修改收据
     * @param receiptNo
     * @param request
     */
    @SecurityAudit
    @PostMapping(value = "/opt/order/modify")
    public void modifyOrderReceipt(@RequestParam(value = "receiptNo") String receiptNo, @Validated(value = {Verify.OrderModifyVerify.class}) @RequestBody OrderModifyRequest request,
                              @Value(value = "#{receiptOrderPaidService}") ReceiptOrderPaidService receiptOrderPaidService) {
        var employee = WebSecurityContext.getSecurity().orElseThrow(() -> new BusinessWarnException(HTTP_SECURITY_DECLINE, "error.security.notFound"));
        var map = new HashMap<String, MatchResult>(request.itemChargeCategories());
        receiptOrderPaidService.modifyReceipt(receiptNo, request, map, employee);
    }

    /**
     * 修改收据
     * @param receiptNo
     * @param request
     */
    @SecurityAudit
    @PostMapping(value = "/opt/refund/modify")
    public void modifyRefundReceipt(@RequestParam(value = "receiptNo") String receiptNo, @Validated @RequestBody RefundOrderModifyRequest request,
                              @Value(value = "#{receiptRefundOrderService}") ReceiptRefundOrderService receiptRefundOrderService) {
        var employee = WebSecurityContext.getSecurity().orElseThrow(() -> new BusinessWarnException(HTTP_SECURITY_DECLINE, "error.security.notFound"));
        receiptRefundOrderService.modifyReceipt(receiptNo, request, null, employee);
    }

    /**
     * 修改收据
     * @param receiptNo
     * @param request
     */
    @SecurityAudit
    @PostMapping(value = "/opt/transfer/modify")
    public void modifyTransferReceipt(@RequestParam(value = "receiptNo") String receiptNo, @Validated @RequestBody TransferOrderModifyRequest request,
                              @Value(value = "#{receiptTransferOrderService}") ReceiptTransferOrderService receiptTransferOrderService) {
        var employee = WebSecurityContext.getSecurity().orElseThrow(() -> new BusinessWarnException(HTTP_SECURITY_DECLINE, "error.security.notFound"));
        receiptTransferOrderService.modifyReceipt(receiptNo, request, null, employee);
    }

    /**
     * 作废收据
     * @param receiptNo
     * @param receiptType
     */
    @SecurityAudit
    @GetMapping(value = "/discard")
    public void discardReceipt(@RequestParam(value = "receiptNo") String receiptNo, @RequestParam(value = "receiptType") String receiptType) {
        var employee = WebSecurityContext.getSecurity().orElseThrow(() -> new BusinessWarnException(HTTP_SECURITY_DECLINE, "error.security.notFound"));
        ReceiptTypeEnum.of(receiptType).getReceiptService(this.applicationContext).discardReceipt(receiptNo, employee);
    }

}
