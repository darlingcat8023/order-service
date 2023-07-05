package com.star.enterprise.order.charge.matcher.business;

import com.querydsl.jpa.impl.JPAQuery;
import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.constants.ChargeCategoryEnum;
import com.star.enterprise.order.charge.matcher.*;
import com.star.enterprise.order.charge.matcher.processor.*;
import com.star.enterprise.order.charge.model.TargetUser;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Stream;

import static com.star.enterprise.order.charge.constants.BusinessTypeEnum.COURSE;
import static com.star.enterprise.order.charge.constants.ChargeCategoryEnum.*;

/**
 * @author xiaowenrou
 * @date 2022/9/22
 */
@Component
public final class CourseChargeBusinessMatcher extends AbstractChargeChainMatcher<CourseChargeBusiness> {

    private final BusinessTypeEnum BUSINESS_TYPE = COURSE;

    private final EmptyMatcher delegate = new EmptyMatcher();

    private final List<PropertyMatchProcessor<CourseChargeBusiness>> processorList = new ArrayList<>();

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public String getBusinessType() {
        return this.BUSINESS_TYPE.value();
    }

    @Override
    public List<PropertyMatchProcessor<CourseChargeBusiness>> getProcessors() {
        return Collections.unmodifiableList(this.processorList);
    }

    @Override
    public ChargeMatcher<CourseChargeBusiness> getEmptyMatcherDelegate() {
        return this.delegate;
    }

    @Override
    public JPAQuery<?> query() {
        return new JPAQuery<>(this.entityManager);
    }

    @Override
    @SuppressWarnings(value = {"unchecked"})
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        this.processorList.add(applicationContext.getBean(ExecutorStatusMatchProcessor.class));
        this.processorList.add(applicationContext.getBean(CourseSpecMatchProcessor.class));
        this.processorList.add(applicationContext.getBean(StartEndDateMatchProcessor.class));
        this.processorList.add(applicationContext.getBean(LimitMatchProcessor.class));
        this.processorList.add(applicationContext.getBean(PriceAttributeMatchProcessor.class));
    }

    public static final class EmptyMatcher implements ChargeMatcher<CourseChargeBusiness> {


        @Override
        public List<MatchResult> matchSingle(TargetUser target, CourseChargeBusiness product) {
            return Stream.of(NEW, RENEW, TRAINING_NEW, EXPANSION, UPGRADE, GOODS, COMPETITION, INTEREST_CLASS, DRAINAGE, COOPERATION, TRANSFER, OTHER)
                    .map(ChargeCategoryEnum::value).map(value -> new ChargeMatchResult(null, value)).map(item -> (MatchResult)item).toList();
        }

        @Override
        public MultiValueMap<String, MatchResult> matchMulti(TargetUser target, Collection<CourseChargeBusiness> products) {
            var ret = new LinkedMultiValueMap<String, MatchResult>();
            products.forEach(product -> ret.addAll(product.uniqueId(), this.matchSingle(target, product)));
            return ret;
        }

        @Override
        public List<ChargeHook> getExecutedHooks(String chargeId, TargetUser target) {
            return List.of();
        }

        @Override
        public List<ChargeHook> getRollbackHooks(String chargeId, TargetUser target) {
            return List.of();
        }

    }

}
