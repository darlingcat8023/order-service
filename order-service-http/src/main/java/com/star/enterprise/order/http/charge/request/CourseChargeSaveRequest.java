package com.star.enterprise.order.http.charge.request;

import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.model.ChargeProperties;
import com.star.enterprise.order.charge.model.OrderChargePayload;
import com.star.enterprise.order.charge.model.impl.DefaultChargeProperties;
import org.springframework.util.Assert;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.star.enterprise.order.base.utils.DateTimeUtils.ZONE_OFFSET;
import static com.star.enterprise.order.charge.constants.rule.BaseChargeRuleSupport.*;
import static com.star.enterprise.order.charge.constants.rule.CourseChargeRuleSupport.*;

/**
 * @author xiaowenrou
 * @date 2022/9/14
 */
public record CourseChargeSaveRequest(

        Long id,

        @NotBlank(message = "business type not valid")
        String businessType,

        @NotBlank(message = "business id not valid")
        String businessId,

        @Valid
        @NotNull(message = "business spec not valid")
        List<BusinessSpec> businessSpecs,

        @NotNull(message = "executor status not valid")
        @Size(message = "executor status not valid", min = 1)
        List<String> executorStatus,

        @Valid
        @NotNull(message = "price info not valid")
        BasePriceInfo priceInfo,

        Integer limit,

        LocalDateTime startTime,

        LocalDateTime endTime,

        @NotNull(message = "campuses not valid")
        List<String> campuses,

        @NotBlank(message = "category not valid")
        String category,

        @NotBlank(message = "enable not valid")
        String enable

) implements OrderChargePayload {

        @Override
        public BusinessTypeEnum type() {
                var type = BusinessTypeEnum.of(this.businessType);
                Assert.notNull(type, "type is null");
                return type;
        }

        @Override
        public List<ChargeProperties> properties() {
                var list = new ArrayList<ChargeProperties>();
                Consumer<BusinessSpec> bsConsumer = item -> list.add(DefaultChargeProperties.builder().property(COURSE_SPEC.getProperty()).propertyDesc(COURSE_SPEC.getPropertyDesc()).standardValue(item.specId()).penetrate(Jackson.writeString(item)).build());
                this.businessSpecs.forEach(bsConsumer);
                Consumer<String> esConsumer = item -> list.add(DefaultChargeProperties.builder().property(EXECUTOR_STATUS.getProperty()).propertyDesc(EXECUTOR_STATUS.getPropertyDesc()).standardValue(item).build());
                this.executorStatus.forEach(esConsumer);
                Consumer<String> cConsumer = item -> list.add(DefaultChargeProperties.builder().property(CAMPUS.getProperty()).propertyDesc(CAMPUS.getPropertyDesc()).standardValue(item).build());
                this.campuses.forEach(cConsumer);
                if (this.limit != null) {
                        list.add(new DefaultChargeProperties(LIMIT.getProperty(), LIMIT.getPropertyDesc(), String.valueOf(this.limit),  null, null, null, null));
                }
                if (this.startTime != null || this.endTime != null) {
                        var builder = DefaultChargeProperties.builder().property(AVAILABLE_TIME.getProperty()).propertyDesc(AVAILABLE_TIME.getPropertyDesc());
                        if (this.startTime != null) {
                                builder.minValue(String.valueOf(this.startTime.toInstant(ZONE_OFFSET).toEpochMilli()));
                        }
                        if (this.endTime != null) {
                                builder.maxValue(String.valueOf(this.endTime.toInstant(ZONE_OFFSET).toEpochMilli()));
                        }
                        list.add(builder.build());
                }
                list.add(this.priceInfo.convertChargeProperties());
                return list;
        }

}
