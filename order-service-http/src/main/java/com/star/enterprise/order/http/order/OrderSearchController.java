package com.star.enterprise.order.http.order;

import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.model.OrderSearchPredicate;
import com.star.enterprise.order.core.model.trans.OrderSummaryTransObject;
import com.star.enterprise.order.core.service.OrderSearchService;
import com.star.enterprise.order.core.service.OrderService;
import com.star.enterprise.order.core.service.OrderDictionaryService;
import com.star.enterprise.order.http.order.request.TargetRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

import static com.star.enterprise.order.core.constants.OrderStatusEnum.TO_BE_PAID;

/**
 * @author xiaowenrou
 * @date 2022/9/22
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/enterprise/order")
public class OrderSearchController {

    private final OrderService orderService;

    private final OrderSearchService searchService;

    /**
     * 订单状态枚举
     * @param dictionaryService
     * @return
     */
    @GetMapping(value = "/dic/status")
    public Map<?, ?> orderStatusMap(@Value(value = "#{orderDictionaryService}") OrderDictionaryService dictionaryService) {
        return dictionaryService.buildOrderStatusDictionary();
    }

    /**
     * 检查订单是否存在
     * @param record
     * @return
     */
    @PostMapping(value = "/exists/frontend")
    public Collection<OrderSummaryTransObject> checkOrderExists(@Validated @RequestBody TargetRecord record) {
        return this.orderService.findOrder(record, TO_BE_PAID);
    }

    /**
     * 获取订单摘要
     * @param orderId
     * @return
     */
    @GetMapping(value = "/summary")
    public OrderSummaryTransObject getOrderSummary(@RequestParam(value = "orderId") String orderId) {
        return this.orderService.getOrderInfoSummaryObject(orderId);
    }

    /**
     * 分页查询订单
     * @param predicate
     * @param rollId 滚动分页的id
     * @param pageable
     */
    @PostMapping(value = "/page")
    public Page<OrderSearchInfoEntity> pageOrderInfo(@RequestBody OrderSearchPredicate predicate, @RequestParam(required = false) String rollId, Pageable pageable) {
        return this.searchService.searchOrder(predicate, rollId, pageable);
    }

}
