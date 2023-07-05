package com.star.enterprise.order.refund.service;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.data.es.entity.SearchedOperator;
import com.star.enterprise.order.core.utils.ApplicationContextUtils;
import com.star.enterprise.order.refund.data.es.OrderRefundInfoSearchRepository;
import com.star.enterprise.order.refund.data.es.entity.OrderRefundSearchInfoEntity;
import com.star.enterprise.order.refund.data.jpa.entity.OrderRefundInfoEntity;
import com.star.enterprise.order.refund.model.RefundOrderSearchPredicate;
import lombok.AllArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
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

import static com.star.enterprise.order.base.exception.RestCode.REFUND_DEEP_DATA;
import static com.star.enterprise.order.refund.constants.OrderRefundStatusEnum.CANCEL;
import static org.apache.lucene.search.join.ScoreMode.None;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Service
@AllArgsConstructor
public class OrderRefundElasticsearchService implements OrderRefundSearchService {

    private final ApplicationContext context;

    private final OrderRefundInfoSearchRepository searchRepository;

    private final ElasticsearchOperations operations;

    public void cancelElasticRefundOrder(String refundOrderId) {
        this.searchRepository.findByRefundOrderId(refundOrderId).ifPresent(e -> this.searchRepository.save(e.setRefundStatus(CANCEL.value())));
    }

    @Override
    public void refresh(String refundOrderId, TargetUser target, OrderRefundInfoEntity orderRefund) {
        var entity = this.searchRepository.findByRefundOrderId(refundOrderId).orElseGet(() -> {
            var e = new OrderRefundSearchInfoEntity();
            if (StringUtils.hasText(orderRefund.getOperator())) {
                e.setOperator(Jackson.read(orderRefund.getOperator(), SearchedOperator.class));
            }
            return e.setRefundOrderId(refundOrderId).setApprovalId(orderRefund.getApprovalId())
                    .setCreatedAt(orderRefund.getCreatedDate()).setRefundStatus(orderRefund.getRefundStatus());
        });
        entity.setUpdatedAt(orderRefund.getLastModifyDate());
        ApplicationContextUtils.getBeans(this.context, OrderRefundAsyncService.class).forEach(service -> service.asyncElastic(refundOrderId, target, entity));
        this.searchRepository.save(entity);
    }

    public Page<OrderRefundSearchInfoEntity> pageRefundOrder(RefundOrderSearchPredicate predicate, String rollId, Pageable pageable) {
        if (pageable.getOffset() > 5000L) {
            throw new BusinessWarnException(REFUND_DEEP_DATA, "error.order.deepData");
        }
        var query = QueryBuilders.boolQuery();
        if (predicate.getTimeRange() != null) {
            var range = predicate.getTimeRange();
            query.must(QueryBuilders.rangeQuery("createdAt").from(range.getFirst()).to(range.getSecond()));
        }
        if (StringUtils.hasText(predicate.getRefundStatus())) {
            query.must(QueryBuilders.termQuery("refundStatus", predicate.getRefundStatus()));
        }
        if (StringUtils.hasText(predicate.getTarget())) {
            var boolQuery = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("target.targetName", predicate.getTarget()))
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
        if (StringUtils.hasText(predicate.getProductName())) {
            query.must(QueryBuilders.nestedQuery("items", QueryBuilders.matchPhraseQuery("items.productName", predicate.getProductName()), None));
        }
        var nativeQuery = new NativeSearchQueryBuilder().withQuery(query)
                .withSort(Sort.by("createdAt").descending())
                .withPageable(pageable).build();
        var hints = this.operations.search(nativeQuery, OrderRefundSearchInfoEntity.class);
        return new PageImpl<>(hints.getSearchHits().stream().map(SearchHit::getContent).toList(), pageable, hints.getTotalHits());
    }

}
