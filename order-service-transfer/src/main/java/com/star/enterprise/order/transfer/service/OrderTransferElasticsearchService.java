package com.star.enterprise.order.transfer.service;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.data.es.entity.SearchedOperator;
import com.star.enterprise.order.core.utils.ApplicationContextUtils;
import com.star.enterprise.order.transfer.data.es.OrderTransferInfoSearchRepository;
import com.star.enterprise.order.transfer.data.es.entity.OrderTransferSearchInfoEntity;
import com.star.enterprise.order.transfer.data.jpa.entity.OrderTransferInfoEntity;
import com.star.enterprise.order.transfer.model.TransferOrderSearchPredicate;
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
import static com.star.enterprise.order.transfer.constants.OrderTransferStatusEnum.CANCEL;
import static org.apache.lucene.search.join.ScoreMode.None;

/**
 * @author xiaowenrou
 * @date 2023/3/7
 */
@Service
@AllArgsConstructor
public class OrderTransferElasticsearchService implements OrderTransferSearchService {

    private final ApplicationContext context;

    private final OrderTransferInfoSearchRepository searchRepository;

    private final ElasticsearchOperations operations;

    public void cancelElasticTransferOrder(String transferOrderId) {
        this.searchRepository.findByTransferOrderId(transferOrderId).ifPresent(e -> this.searchRepository.save(e.setTransferStatus(CANCEL.value())));
    }

    @Override
    public void refresh(String transferOrderId, TargetUser target, OrderTransferInfoEntity orderTransfer) {
        var entity = this.searchRepository.findByTransferOrderId(transferOrderId).orElseGet(() -> {
            var e = new OrderTransferSearchInfoEntity();
            if (StringUtils.hasText(orderTransfer.getOperator())) {
                e.setOperator(Jackson.read(orderTransfer.getOperator(), SearchedOperator.class));
            }
            return e.setTransferOrderId(transferOrderId).setApprovalId(orderTransfer.getApprovalId())
                    .setCreatedAt(orderTransfer.getCreatedDate()).setTransferStatus(orderTransfer.getTransferStatus());
        });
        entity.setUpdatedAt(orderTransfer.getLastModifyDate());
        ApplicationContextUtils.getBeans(this.context, OrderTransferAsyncService.class).forEach(service -> service.asyncElastic(transferOrderId, target, entity));
        this.searchRepository.save(entity);
    }

    public Page<OrderTransferSearchInfoEntity> pageTransferOrder(TransferOrderSearchPredicate predicate, String rollId, Pageable pageable) {
        if (pageable.getOffset() > 5000L) {
            throw new BusinessWarnException(REFUND_DEEP_DATA, "error.order.deepData");
        }
        var query = QueryBuilders.boolQuery();
        if (predicate.getTimeRange() != null) {
            var range = predicate.getTimeRange();
            query.must(QueryBuilders.rangeQuery("createdAt").from(range.getFirst()).to(range.getSecond()));
        }
        if (StringUtils.hasText(predicate.getRefundStatus())) {
            query.must(QueryBuilders.termQuery("transferStatus", predicate.getRefundStatus()));
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
        var hints = this.operations.search(nativeQuery, OrderTransferSearchInfoEntity.class);
        return new PageImpl<>(hints.getSearchHits().stream().map(SearchHit::getContent).toList(), pageable, hints.getTotalHits());
    }

}
