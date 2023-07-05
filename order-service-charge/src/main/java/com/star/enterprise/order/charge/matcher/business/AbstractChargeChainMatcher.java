package com.star.enterprise.order.charge.matcher.business;

import com.querydsl.jpa.impl.JPAQuery;
import com.star.enterprise.order.base.Paired;
import com.star.enterprise.order.charge.data.jpa.ChargeItemRepository;
import com.star.enterprise.order.charge.data.jpa.ChargePropertiesRepository;
import com.star.enterprise.order.charge.data.jpa.entity.QChargeItemEntity;
import com.star.enterprise.order.charge.data.jpa.entity.QChargePropertiesEntity;
import com.star.enterprise.order.charge.matcher.*;
import com.star.enterprise.order.charge.model.ChargeProperties;
import com.star.enterprise.order.charge.model.TargetUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.star.enterprise.order.charge.constants.rule.BaseChargeRuleSupport.CAMPUS;

/**
 * @author xiaowenrou
 * @date 2022/9/22
 */
public abstract sealed class AbstractChargeChainMatcher<T extends Business> implements ChargeMatcher<T>, ApplicationContextAware
        permits CourseChargeBusinessMatcher {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ChargeItemRepository chargeItemRepository;

    private ChargePropertiesRepository chargePropertiesRepository;

    /**
     * 空匹配的委托
     */
    private final ChargeMatcher<? extends Business> delegate = new EmptyMatcher();

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (this.chargeItemRepository == null) {
            this.chargeItemRepository = applicationContext.getBean(ChargeItemRepository.class);
        }
        if (this.chargePropertiesRepository == null) {
            this.chargePropertiesRepository = applicationContext.getBean(ChargePropertiesRepository.class);
        }
    }

    @Override
    public List<MatchResult> matchSingle(TargetUser target, T product) {
        logger.info("start matching charge properties target = {}, product = {}", target, product);
        if (!this.hasRules(target, product)) {
            return this.getEmptyMatcherDelegate().matchSingle(target, product);
        }
        var list = this.gatAvailableCategories(target, product.uniqueId());
        if (CollectionUtils.isEmpty(list)) {
            // 委托到空方法
            return List.of();
        }
        var result = new ArrayList<MatchResult>();
        list.forEach(pair -> {
            var category = pair.getFirst();
            var itemId = pair.getSecond();
            var propMap = this.getPropertiesMap(itemId);
            if (this.getProcessors().parallelStream().allMatch(proc -> proc.process(target, product, itemId, propMap))) {
                result.add(new ChargeMatchResult(itemId, category));
            }
        });
        return result;
    }

    @Override
    public MultiValueMap<String, MatchResult> matchMulti(TargetUser target, Collection<T> products) {
        var ret = new LinkedMultiValueMap<String, MatchResult>();
        for (T product : products) {
            ret.addAll(product.uniqueId(), this.matchSingle(target, product));
        }
        return ret;
    }

    @Override
    public List<ChargeHook> getExecutedHooks(String chargeId, TargetUser target) {
        var propertiesMap = this.getPropertiesMap(chargeId);
        var list = new ArrayList<ChargeHook>();
        this.getProcessors().forEach(proc -> proc.getExecuteHook(target, chargeId, propertiesMap).ifPresent(list::add));
        return list;
    }

    @Override
    public List<ChargeHook> getRollbackHooks(String chargeId, TargetUser target) {
        var propertiesMap = this.getPropertiesMap(chargeId);
        var list = new ArrayList<ChargeHook>();
        this.getProcessors().forEach(proc -> proc.getRollbackHook(target, chargeId, propertiesMap).ifPresent(list::add));
        return list;
    }

    /**
     * 获取业务类型
     * @return
     */
    public abstract String getBusinessType();

    /**
     * 获取责任链
     * @return
     */
    public abstract List<PropertyMatchProcessor<T>> getProcessors();

    /**
     * 默认的委托匹配器
     * @return
     */
    public abstract ChargeMatcher<T> getEmptyMatcherDelegate();

    /**
     * 获取querydsl
     * @return
     */
    public abstract JPAQuery<?> query();

    /**
     * 检查是否有配置了规则
     * @param target
     * @param product
     * @return
     */
    public boolean hasRules(TargetUser target, T product) {
        var ent = QChargeItemEntity.chargeItemEntity;
        var predicate = ent.enable.eq("enable").and(ent.businessId.eq(product.uniqueId())).and(ent.businessType.eq(this.getBusinessType()));
        return this.chargeItemRepository.exists(predicate);
    }

    /**
     * 获取所有可匹配的类型
     * @param businessId
     * @return
     */
    public List<Paired<String, String>> gatAvailableCategories(TargetUser target, String businessId) {
        var query = this.query();
        var cie = QChargeItemEntity.chargeItemEntity;
        var cpe = QChargePropertiesEntity.chargePropertiesEntity;
        // 只查询授权给当前校区的规则
        var res = query.select(cie.chargeItemId, cie.category).from(cie).leftJoin(cpe).on(cie.chargeItemId.eq(cpe.chargeItemId))
                .where(cie.enable.eq("enable"), cie.businessId.eq(businessId), cie.businessType.eq(this.getBusinessType()), cpe.property.eq(CAMPUS.getProperty()), cpe.standardValue.eq(target.campus()))
                .fetch();
        if (CollectionUtils.isEmpty(res)) {
            return List.of();
        }
        return res.stream().map(tuple -> Paired.of(tuple.get(cie.category), tuple.get(cie.chargeItemId))).toList();
    }

    public MultiValueMap<String, ChargeProperties> getPropertiesMap(String chargeItemId) {
        var props = this.chargePropertiesRepository.findByChargeItemId(chargeItemId);
        var multiValueMap = new LinkedMultiValueMap<String, ChargeProperties>();
        props.forEach(p -> multiValueMap.add(p.getProperty(), p));
        return multiValueMap;
    }

}
