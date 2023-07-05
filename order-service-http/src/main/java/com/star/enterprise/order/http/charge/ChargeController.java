package com.star.enterprise.order.http.charge;

import com.star.enterprise.order.base.Rest;
import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.model.ChargeProperties;
import com.star.enterprise.order.charge.service.ChargeDictionaryService;
import com.star.enterprise.order.charge.service.ChargeService;
import com.star.enterprise.order.http.charge.request.CampusSaveRequest;
import com.star.enterprise.order.http.charge.request.ChargeStatusRequest;
import com.star.enterprise.order.http.charge.request.CourseChargeSaveRequest;
import com.star.enterprise.order.http.charge.response.PageCourseChargeItemResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.star.enterprise.order.charge.constants.rule.BaseChargeRuleSupport.CAMPUS;

/**
 * @author xiaowenrou
 * @date 2022/9/14
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/enterprise/order/charge")
public class ChargeController {

    private final ChargeService chargeService;

    /**
     * 费用类型枚举
     * @param chargeDictionaryService
     * @return
     */
    @GetMapping(value = "/dic/chargeCategory")
    public Map<?, ?> chargeTypeMap(@Value(value = "#{chargeDictionaryService}") ChargeDictionaryService chargeDictionaryService) {
        return chargeDictionaryService.buildChargeCategoryDictionary();
    }

    /**
     * 价格设置枚举
     * @param chargeDictionaryService
     * @return
     */
    @GetMapping(value = "/dic/feeAttr")
    public Map<?, ?> feeAttributeMap(@Value(value = "#{chargeDictionaryService}") ChargeDictionaryService chargeDictionaryService) {
        return chargeDictionaryService.buildFeeAttributeDictionary();
    }

    /**
     * 业务类型枚举
     * @param chargeDictionaryService
     * @return
     */
    @GetMapping(value = "/dic/businessType")
    public Map<?, ?> businessMap(@Value(value = "#{chargeDictionaryService}") ChargeDictionaryService chargeDictionaryService) {
        return chargeDictionaryService.buildBusinessTypeDictionary();
    }

    /**
     * 保存收费类型
     * @param request
     * @return
     */
    @PostMapping(value = "/opt/course/save")
    public Rest<?> saveCourseCharge(@Validated @RequestBody CourseChargeSaveRequest request)  {
        return Rest.just(this.chargeService.saveChargeItem(request.id(), request, request.enable()));
    }

    /**
     * 保存授权校区
     * @return
     */
    @PostMapping(value = "/opt/{businessType}/saveCampus")
    public Rest<?> saveChargeCampus(@PathVariable(value = "businessType") String businessType, @RequestBody CampusSaveRequest request) {
        var type = BusinessTypeEnum.of(businessType);
        Assert.notNull(type, "type is null");
        return Rest.just(this.chargeService.saveSingleChargeProperties(type, request.chargeItemId(), request.toProperties(), true));
    }

    /**
     * 收费类型列表
     * @param businessId
     * @param enable
     * @param pageable
     * @return
     */
    @GetMapping(value = "/opt/course/page")
    public Page<?> pageCourseCharge(@RequestParam(value = "businessId") String businessId, @RequestParam(value = "enable", required = false) String enable, Pageable pageable) {
        return this.chargeService.pageChargeItem(BusinessTypeEnum.COURSE.value(), businessId, enable, pageable, null).map(PageCourseChargeItemResponse::buildResponse);
    }

    /**
     * 授权校区列表
     * @param businessType
     * @param chargeItemId
     * @return
     */
    @GetMapping(value = "/opt/{businessType}/listCampus")
    public List<?> propertyList(@PathVariable(value = "businessType") String businessType, @RequestParam(value = "chargeItemId") String chargeItemId) {
        var type = BusinessTypeEnum.of(businessType);
        Assert.notNull(type, "type is null");
        return this.chargeService.streamProperties(chargeItemId, List.of(CAMPUS.getProperty())).stream().map(ChargeProperties::getStandardValue).toList();
    }

    /**
     * 批量更改状态
     * @param request
     */
    @PostMapping(value = "/opt/status")
    public void status(@Validated @RequestBody ChargeStatusRequest request) {
        this.chargeService.changeStatus(request.state(), request.idPairs());
    }


}
