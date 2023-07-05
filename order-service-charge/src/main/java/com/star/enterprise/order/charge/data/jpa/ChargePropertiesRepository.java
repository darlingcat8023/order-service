package com.star.enterprise.order.charge.data.jpa;

import com.star.enterprise.order.charge.data.jpa.entity.ChargePropertiesEntity;
import com.star.enterprise.order.charge.model.ChargeProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/9/14
 */
public interface ChargePropertiesRepository extends JpaRepository<ChargePropertiesEntity, Long> {

    /**
     *  根据id查找
     * @param chargeItemId
     * @return
     */
    List<ChargeProperties> findByChargeItemId(String chargeItemId);

    /**
     * 根据id和属性名共同过滤
     * @param chargeItemId
     * @param property
     * @return
     */
    List<ChargeProperties> findByChargeItemIdAndPropertyIn(String chargeItemId, Collection<String> property);

    /**
     * 根据item_id删除
     * @param chargeItemId
     */
    void deleteByChargeItemId(String chargeItemId);

    /**
     * 根据单个属性删除
     * @param chargeItemId
     * @param property
     */
    void deleteByChargeItemIdAndProperty(String chargeItemId, String property);

}