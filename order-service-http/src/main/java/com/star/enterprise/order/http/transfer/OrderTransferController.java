package com.star.enterprise.order.http.transfer;

import com.star.enterprise.order.http.order.request.TargetRecord;
import com.star.enterprise.order.http.transfer.response.OrderTransferItemRecord;
import com.star.enterprise.order.receipt.service.ReceiptOrderPaidService;
import com.star.enterprise.order.transfer.data.es.entity.OrderTransferSearchInfoEntity;
import com.star.enterprise.order.transfer.model.TransferOrderSearchPredicate;
import com.star.enterprise.order.transfer.model.trans.OrderTransferSummaryTransObject;
import com.star.enterprise.order.transfer.service.OrderTransferSearchService;
import com.star.enterprise.order.transfer.service.OrderTransferService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaowenrou
 * @date 2023/3/2
 */
@RestController
@RequestMapping(value = "/api/enterprise/order/transfer")
@AllArgsConstructor
public class OrderTransferController {

    private final OrderTransferService transferService;

    private final ReceiptOrderPaidService receiptService;

    private final OrderTransferSearchService searchService;

    /**
     * 获取可用的退费列表
     * @param targetId
     * @param campus
     * @param containsMatch
     * @param containsArticle
     * @return
     */
    @GetMapping(value = "/items")
    public List<OrderTransferItemRecord> refundItems(@RequestParam(value = "targetId") String targetId, @RequestParam(value = "campus") String campus,
                                                     @RequestParam(value = "containsMatch", required = false, defaultValue = "true") boolean containsMatch,
                                                     @RequestParam(value = "containsArticle", required = false, defaultValue = "true") boolean containsArticle) {
        var target = new TargetRecord(targetId, campus, null, null);
        var transferList = this.transferService.findAvailableTransferOrder(target, containsMatch, containsArticle);
        var map = this.receiptService.queryReceiptNo(transferList.stream().map(item -> item.getObject().getOrderId()).collect(Collectors.toSet()));
        var ret = new ArrayList<OrderTransferItemRecord>();
        transferList.forEach(refund -> refund.getRefundItems().forEach(item -> {
            var object = refund.getObject();
            var fee = item.getItemFee();
            var balance = item.getNumberLeft().multiply(fee.getDueCollectSinglePrice());
            var record = new OrderTransferItemRecord(map.get(item.getOrderId()), item.getOrderId(), object.getAdditional().getInvoiceNo(), object.getCompletedDate(),
                    item.getOrderItemId(), item.getBusinessId(), item.getBusinessType(), item.getProductName(), new BigDecimal(item.getNumber()), new BigDecimal(item.getApportion()),
                    item.getNumberLeft(), item.getApportionLeft(), balance, fee.getOriginSinglePrice(), fee.getDueCollectSinglePrice(), item.getCurrentStandardPrice(), item.getDelegate());
            ret.add(record);
        }));
        return ret;
    }

    /**
     * 分页查询
     * @param predicate
     * @param rollId
     * @param pageable
     * @return
     */
    @PostMapping(value = "/page")
    public Page<OrderTransferSearchInfoEntity> pageRefundOrder(@RequestBody TransferOrderSearchPredicate predicate, @RequestParam(required = false) String rollId, Pageable pageable) {
        return this.searchService.pageTransferOrder(predicate, rollId, pageable);
    }

    /**
     * 获取退费订单摘要
     * @param orderId
     * @return
     */
    @GetMapping(value = "/summary")
    public OrderTransferSummaryTransObject getOrderRefundSummary(@RequestParam(value = "transferOrderId") String orderId) {
        return this.transferService.getSummaryObject(orderId);
    }

}
