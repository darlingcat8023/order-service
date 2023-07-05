package com.star.enterprise.order.receipt.service;

import com.star.enterprise.order.core.data.es.entity.SearchedTarget;
import com.star.enterprise.order.receipt.data.es.ReceiptSearchInfoRepository;
import com.star.enterprise.order.receipt.data.es.entity.ReceiptSearchInfoEntity;
import com.star.enterprise.order.receipt.model.ReceiptSearchPredicate;
import lombok.AllArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
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

import java.util.Set;

import static org.apache.lucene.search.join.ScoreMode.None;

/**
 * @author xiaowenrou
 * @date 2022/12/6
 */
@Service
@AllArgsConstructor
public class ReceiptElasticService implements ReceiptSearchService {

    private final ReceiptSearchInfoRepository searchInfoRepository;

    private final ElasticsearchOperations operations;

    private final ApplicationContext applicationContext;

    @Override
    public Page<ReceiptSearchInfoEntity> searchReceipt(ReceiptSearchPredicate predicate, String rollId, Pageable pageable) {
        var query = QueryBuilders.boolQuery();
        this.buildQuery(query, predicate);
        var nativeQuery = new NativeSearchQueryBuilder().withQuery(query).withSort(Sort.by("createdAt").descending()).withPageable(pageable);
        var result = this.operations.search(nativeQuery.build(), ReceiptSearchInfoEntity.class);
        return new PageImpl<>(result.getSearchHits().stream().map(SearchHit::getContent).toList(), pageable, result.getTotalHits());
    }

    private void buildQuery(BoolQueryBuilder query, ReceiptSearchPredicate predicate) {
        if (!CollectionUtils.isEmpty(predicate.getReceiptType())) {
            var boolQuery = QueryBuilders.boolQuery();
            predicate.getReceiptType().forEach(type -> boolQuery.should(QueryBuilders.termQuery("receiptType", type)));
            query.must(boolQuery);
        }
        if (StringUtils.hasText(predicate.getReceiptNo())) {
            query.must(QueryBuilders.wildcardQuery("receiptNo", "*" + predicate.getReceiptNo() + "*"));
        }
        if (StringUtils.hasText(predicate.getStatus())) {
            query.must(QueryBuilders.termQuery("receiptStatus", predicate.getStatus()));
        }
        if (StringUtils.hasText(predicate.getOperator())) {
            query.must(QueryBuilders.matchQuery("operators", predicate.getOperator()));
        }
        if (predicate.getCompletedDateRange() != null) {
            var range = predicate.getCompletedDateRange();
            query.must(QueryBuilders.rangeQuery("createdAt").from(range.getFirst()).to(range.getSecond()));
        }
        if (StringUtils.hasText(predicate.getTarget())) {
            var boolQuery = QueryBuilders.boolQuery().should(QueryBuilders.matchPhraseQuery("target.targetName", predicate.getTarget()))
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
        if (predicate.getTargetChannel() != null) {
            var chn = predicate.getTargetChannel();
            if (StringUtils.hasText(chn.getSecond())) {
                query.must(QueryBuilders.termQuery("target.subChannelId", chn.getSecond()));
            } else if (StringUtils.hasText(chn.getFirst())) {
                query.must(QueryBuilders.termQuery("target.channelId", chn.getFirst()));
            }
        }
        if (StringUtils.hasText(predicate.getRemark())) {
            query.should(QueryBuilders.matchPhraseQuery("additional.innerRemark", predicate.getRemark()))
                    .should(QueryBuilders.matchPhraseQuery("additional.outerRemark", predicate.getRemark()));
        }
        if (StringUtils.hasText(predicate.getInvoiceNo())) {
            query.must(QueryBuilders.wildcardQuery("additional.invoiceNo", predicate.getInvoiceNo() + "*"));
        }
        if (StringUtils.hasText(predicate.getProductName()) || StringUtils.hasText(predicate.getChargeType())) {
            var bq = QueryBuilders.boolQuery();
            if (StringUtils.hasText(predicate.getProductName())) {
                bq.must(QueryBuilders.matchPhraseQuery("items.productName", predicate.getProductName()));
            }
            if (StringUtils.hasText(predicate.getChargeType())) {
                bq.must(QueryBuilders.termQuery("items.chargeCategory", predicate.getChargeType()));
            }
            query.must(QueryBuilders.nestedQuery("items", bq, None));
        }
        if (StringUtils.hasText(predicate.getPaymentMethod())) {
            query.must(QueryBuilders.nestedQuery("payments", QueryBuilders.termQuery("payments.paymentMethod", predicate.getPaymentMethod()), None));
        }
        if (StringUtils.hasText(predicate.getPerformanceUserId())) {
            query.must(QueryBuilders.nestedQuery("performanceUsers", QueryBuilders.wildcardQuery("performanceUsers.userId", predicate.getPerformanceUserId() + "*"), None));
        }
    }

    public void refresh(Receipt receipt, Set<String> operators) {
        var entity = this.searchInfoRepository.findByReceiptNo(receipt.receiptNo()).orElseGet(ReceiptSearchInfoEntity::new).setPrint(receipt.print())
                .setReceiptNo(receipt.receiptNo()).setReceiptType(receipt.type().value()).setOperators(operators).setReceiptStatus(receipt.status().value());
        var target = receipt.target();
        var ctx = target.context();
        // 设置目标用户
        var st = new SearchedTarget().setTargetId(target.targetId()).setTargetMobile(target.context().getUserMobile())
                .setTargetNumber(target.context().getUserNumber()).setTargetName(target.context().getUserName())
                .setTargetCampus(target.campus()).setTargetCampusName(ctx.getCampusName()).setChannelId(ctx.getChannelId())
                .setSubChannelId(ctx.getSubChannelId()).setChannelName(ctx.getChannelName()).setSubChannelName(ctx.getSubChannelName());
        entity.setTarget(st);
        receipt.type().getReceiptService(this.applicationContext).buildSyncData(receipt, entity);
        this.searchInfoRepository.save(entity);
    }

}
