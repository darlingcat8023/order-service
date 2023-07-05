package com.star.enterprise.order.http.refund;

import com.star.enterprise.order.http.order.request.TargetRecord;
import com.star.enterprise.order.http.refund.response.OrderRefundItemRecord;
import com.star.enterprise.order.receipt.service.ReceiptOrderPaidService;
import com.star.enterprise.order.refund.data.es.entity.OrderRefundSearchInfoEntity;
import com.star.enterprise.order.refund.model.RefundOrderSearchPredicate;
import com.star.enterprise.order.refund.model.trans.OrderRefundSummaryTransObject;
import com.star.enterprise.order.refund.service.OrderRefundSearchService;
import com.star.enterprise.order.refund.service.OrderRefundService;
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
@RequestMapping(value = "/api/enterprise/order/refund")
@AllArgsConstructor
public class OrderRefundController {

    private final OrderRefundService refundService;

    private final ReceiptOrderPaidService receiptService;

    private final OrderRefundSearchService searchService;

    /**
     * 获取可用的退费列表
     * @param targetId
     * @param campus
     * @param containsMatch
     * @param containsArticle
     * @return
     */
    @GetMapping(value = "/items")
    public List<OrderRefundItemRecord> refundItems(@RequestParam(value = "targetId") String targetId, @RequestParam(value = "campus") String campus,
                                                   @RequestParam(value = "containsMatch", required = false, defaultValue = "true") boolean containsMatch,
                                                   @RequestParam(value = "containsArticle", required = false, defaultValue = "true") boolean containsArticle) {
        var target = new TargetRecord(targetId, campus, null, null);
        var refundList = this.refundService.findAvailableRefundOrder(target, containsMatch, containsArticle);
        var map = this.receiptService.queryReceiptNo(refundList.stream().map(item -> item.getObject().getOrderId()).collect(Collectors.toSet()));
        var ret = new ArrayList<OrderRefundItemRecord>();
        refundList.forEach(refund -> refund.getRefundItems().forEach(item -> {
            var object = refund.getObject();
            var fee = item.getItemFee();
            var balance = item.getNumberLeft().multiply(fee.getDueCollectSinglePrice());
            var record = new OrderRefundItemRecord(map.get(item.getOrderId()), item.getOrderId(), object.getAdditional().getInvoiceNo(), object.getCompletedDate(),
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
    public Page<OrderRefundSearchInfoEntity> pageRefundOrder(@RequestBody RefundOrderSearchPredicate predicate, @RequestParam(required = false) String rollId, Pageable pageable) {
        return this.searchService.pageRefundOrder(predicate, rollId, pageable);
    }

    /**
     * 获取退费订单摘要
     * @param orderId
     * @return
     */
    @GetMapping(value = "/summary")
    public OrderRefundSummaryTransObject getOrderRefundSummary(@RequestParam(value = "refundOrderId") String orderId) {
        return this.refundService.getSummaryObject(orderId);
    }

}
