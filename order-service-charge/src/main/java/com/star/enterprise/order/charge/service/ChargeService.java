package com.star.enterprise.order.charge.service;

import com.star.enterprise.order.base.exception.BusinessErrorException;
import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.data.jpa.ChargeItemRepository;
import com.star.enterprise.order.charge.data.jpa.ChargePropertiesRepository;
import com.star.enterprise.order.charge.data.jpa.entity.ChargeItemEntity;
import com.star.enterprise.order.charge.data.jpa.entity.ChargePropertiesEntity;
import com.star.enterprise.order.charge.model.ChargeProperties;
import com.star.enterprise.order.charge.model.OrderChargePayload;
import com.star.enterprise.order.charge.model.impl.DefaultChargeItemDetail;
import lombok.AllArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.star.enterprise.order.base.exception.RestCode.CHARGE_ITEM_NOT_FOUND;
import static com.star.enterprise.order.base.exception.RestCode.CHARGE_OPERATE_ERROR;

/**
 * @author xiaowenrou
 * @date 2022/9/14
 */
@Service
@AllArgsConstructor
public class ChargeService {

    private final ChargeItemRepository chargeItemRepository;

    private final ChargePropertiesRepository chargePropertiesRepository;

    private ChargeService currentProxy() {
        return (ChargeService) AopContext.currentProxy();
    }

    @Transactional(rollbackFor = {Exception.class})
    public String saveChargeItem(final Long id, OrderChargePayload payload, String enable) {
        ChargeItemEntity entity;
        if (id != null) {
            entity = this.chargeItemRepository.findById(id).orElseThrow(() -> new BusinessWarnException(CHARGE_ITEM_NOT_FOUND, "error.charge.itemNotFound"));
        } else {
            entity = new ChargeItemEntity().setBusinessType(payload.type().value()).setBusinessId(payload.businessId());
        }
        this.chargeItemRepository.saveAndFlush(entity.setCategory(payload.category()).setEnable(enable));
        if (!StringUtils.hasText(entity.getChargeItemId())) {
            throw new BusinessErrorException(CHARGE_OPERATE_ERROR, "error.charge.saveFail");
        }
        return this.currentProxy().saveMultiChargeProperties(payload.type(), entity.getChargeItemId(), payload.properties(), id != null && id > 0);
    }

    @Transactional(rollbackFor = {Exception.class})
    public String saveMultiChargeProperties(final BusinessTypeEnum businessType, final String chargeItemId, Collection<ChargeProperties> chargeProperties, boolean update) {
        if (update) {
            this.chargePropertiesRepository.deleteByChargeItemId(chargeItemId);
        }
        var function = this.buildFunction(businessType, chargeItemId);
        this.chargePropertiesRepository.saveAllAndFlush(chargeProperties.stream().map(function).toList());
        return chargeItemId;
    }

    @Transactional(rollbackFor = {Exception.class})
    public String saveSingleChargeProperties(final BusinessTypeEnum businessType, final String chargeItemId, Collection<ChargeProperties> chargeProperties, boolean update) {
        Map<String, List<ChargeProperties>> map = chargeProperties.stream().collect(Collectors.groupingBy(ChargeProperties::getProperty));
        map.forEach((property, list) -> {
            if (update) {
                this.chargePropertiesRepository.deleteByChargeItemIdAndProperty(chargeItemId, property);
            }
            var function = this.buildFunction(businessType, chargeItemId);
            this.chargePropertiesRepository.saveAllAndFlush(list.stream().map(function).toList());
        });
        return chargeItemId;
    }

    private Function<ChargeProperties, ChargePropertiesEntity> buildFunction(final BusinessTypeEnum businessType, final String chargeItemId) {
        return item -> new ChargePropertiesEntity().setChargeItemId(chargeItemId).setBusinessType(businessType.value())
                .setProperty(item.getProperty()).setPropertyDesc(item.getPropertyDesc())
                .setStandardValue(item.getStandardValue()).setMinValue(item.getMinValue()).setMaxValue(item.getMaxValue())
                .setAttribute(item.getAttribute()).setPenetrate(item.getPenetrate());
    }

    public Page<DefaultChargeItemDetail> pageChargeItem(String businessType, String businessId, String enable, Pageable pageable, List<String> properties) {
        var entity = new ChargeItemEntity().setBusinessType(businessType).setBusinessId(businessId).setEnable(enable);
        var service= this.currentProxy();
        return this.chargeItemRepository.findBy(Example.of(entity, ExampleMatcher.matching().withIgnoreNullValues()), fluent -> fluent.sortBy(Sort.by("id").descending()).page(pageable))
                .map(item -> new DefaultChargeItemDetail(item).setProperties(service.streamProperties(item.getChargeItemId(), properties)));
    }

    public List<ChargeProperties> streamProperties(final String chargeItemId, List<String> properties) {
        if (CollectionUtils.isEmpty(properties)) {
            return this.chargePropertiesRepository.findByChargeItemId(chargeItemId);
        } else {
            return this.chargePropertiesRepository.findByChargeItemIdAndPropertyIn(chargeItemId, properties);
        }
    }

    public void changeStatus(final String state, List<String> idPairs) {
        var list = new ArrayList<ChargeItemEntity>();
        idPairs.forEach(id -> this.chargeItemRepository.findByChargeItemId(id).map(item -> item.setEnable(state)).ifPresent(list::add));
        if (!list.isEmpty()) {
            this.chargeItemRepository.saveAllAndFlush(list);
        }
    }

}
