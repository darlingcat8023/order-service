package com.star.enterprise.order.core.service;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.constants.OrderStatusEnum;
import com.star.enterprise.order.core.data.es.OrderSearchInfoRepository;
import com.star.enterprise.order.core.data.es.entity.OrderSearchInfoEntity;
import com.star.enterprise.order.core.data.es.entity.SearchedOperator;
import com.star.enterprise.order.core.data.jpa.entity.OrderInfoEntity;
import com.star.enterprise.order.core.model.OrderExtendInfo;
import com.star.enterprise.order.core.model.OrderSearchPredicate;
import com.star.enterprise.order.core.utils.ApplicationContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import static com.star.enterprise.order.base.exception.RestCode.CORE_DEEP_DATA;
import static org.apache.lucene.search.join.ScoreMode.None;

/**
 * 订单信息写入es服务
 * @author xiaowenrou
 * @date 2022/10/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderElasticsearchService implements OrderSearchService, OrderVerifyService {

    private final ApplicationContext applicationContext;

    private final OrderSearchInfoRepository searchInfoRepository;

    private final ElasticsearchOperations operations;

    public void modifyElasticOrderStatus(String orderId, OrderStatusEnum state) {
        this.searchInfoRepository.findByOrderId(orderId).ifPresent(e -> this.searchInfoRepository.save(e.setStatus(state.value())));
    }

    @Override
    public Page<OrderSearchInfoEntity> searchOrder(OrderSearchPredicate predicate, String rollId, Pageable pageable) {
        if (pageable.getOffset() > 5000L) {
            throw new BusinessWarnException(CORE_DEEP_DATA, "error.order.deepData");
        }
        var nativeQuery = new NativeSearchQueryBuilder().withQuery(this.buildQuery(predicate))
                .withSort(Sort.by("createdAt").descending())
                .withPageable(pageable).build();
        var hints = this.operations.search(nativeQuery, OrderSearchInfoEntity.class);
        return new PageImpl<>(hints.getSearchHits().stream().map(SearchHit::getContent).toList(), pageable, hints.getTotalHits());
    }

    private QueryBuilder buildQuery(OrderSearchPredicate predicate) {
        var query = QueryBuilders.boolQuery();
        if (StringUtils.hasText(predicate.getOrderId())) {
            query.must(QueryBuilders.wildcardQuery("orderId", predicate.getOrderId() + "*"));
        }
        if (predicate.getCompletedDateRange() != null) {
            var range = predicate.getCompletedDateRange();
            query.must(QueryBuilders.rangeQuery("completedDate").from(range.getFirst()).to(range.getSecond()));
        }
        if (predicate.getCreatedDateRange() != null) {
            var range = predicate.getCreatedDateRange();
            query.must(QueryBuilders.rangeQuery("createdAt").from(range.getFirst()).to(range.getSecond()));
        }
        if (StringUtils.hasText(predicate.getStatus())) {
            query.must(QueryBuilders.termQuery("status", predicate.getStatus()));
        }
        if (StringUtils.hasText(predicate.getPaymentMethod())) {
            query.must(QueryBuilders.nestedQuery("payments", QueryBuilders.termQuery("payments.paymentMethod", predicate.getPaymentMethod()), None));
        }
        if (StringUtils.hasText(predicate.getTarget())) {
            var boolQuery = QueryBuilders.boolQuery()
                    .should(QueryBuilders.matchQuery("target.targetName", predicate.getTarget()))
                    .should(QueryBuilders.wildcardQuery("target.targetId", "*" + predicate.getTarget() + "*"))
                    .should(QueryBuilders.wildcardQuery("target.targetMobile", "*" + predicate.getTarget() + "*"))
                    .should(QueryBuilders.wildcardQuery("target.targetNumber", "*" + predicate.getTarget() + "*"));
            query.must(boolQuery);
        }
        if (!CollectionUtils.isEmpty(predicate.getSelectedCampus())) {
            var boolQuery = QueryBuilders.boolQuery();
            predicate.getSelectedCampus().forEach(campus -> boolQuery.should(QueryBuilders.termQuery("target.targetCampus", campus)));
            query.must(boolQuery);
        }
        if (StringUtils.hasText(predicate.getCreatedByUserId())) {
            var boolQuery = QueryBuilders.boolQuery()
                    .should(QueryBuilders.wildcardQuery("operator.operatorId", "*" + predicate.getCreatedByUserId() + "*"))
                    .should(QueryBuilders.matchQuery("operator.operatorName", predicate.getCreatedByUserId()));
            query.must(boolQuery);
        }
        if (StringUtils.hasText(predicate.getPerformanceUserId())) {
            var nestedQuery = QueryBuilders.boolQuery()
                    .should(QueryBuilders.wildcardQuery("performanceUsers.userId", "*" + predicate.getPerformanceUserId() + "*"))
                    .should(QueryBuilders.matchQuery("performanceUsers.userName", predicate.getPerformanceUserId()));
            query.must(QueryBuilders.nestedQuery("performanceUsers", nestedQuery, None));
        }
        if (StringUtils.hasText(predicate.getProductName())) {
            query.must(QueryBuilders.nestedQuery("items", QueryBuilders.matchPhraseQuery("items.productName", predicate.getProductName()), None));
        }
        return query;
    }

    @Override
    public void refresh(String orderId, TargetUser target, OrderInfoEntity order) {
        final var esInfo = this.searchInfoRepository.findByOrderId(orderId).orElseGet(() -> {
            var e = new OrderSearchInfoEntity();
            if (StringUtils.hasText(order.getLastModifiedBy())) {
                e.setOperator(Jackson.read(order.getLastModifiedBy(), SearchedOperator.class));
            }
            return e;
        });
        BeanUtils.copyProperties(order, esInfo);
        ApplicationContextUtils.getBeans(this.applicationContext, OrderAsyncService.class).forEach(service -> service.asyncElastic(orderId, target, esInfo));
        this.searchInfoRepository.save(esInfo);
    }

    @Override
    public void afterExistOrderModify(String orderId, TargetUser targetUser, OrderExtendInfo extendInfo, OrderInfoEntity entity) {
        this.refresh(orderId, targetUser, entity);
        OrderVerifyService.super.afterExistOrderModify(orderId, targetUser, extendInfo, entity);
    }
}
