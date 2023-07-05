package com.star.enterprise.order.core.calculator.provider;

import com.star.enterprise.order.base.exception.BusinessWarnException;
import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.core.calculator.CalculateProcessor;
import com.star.enterprise.order.core.calculator.CalculatorProcessorChain;
import com.star.enterprise.order.core.calculator.OrderFeeDetail;
import com.star.enterprise.order.core.calculator.processor.*;
import com.star.enterprise.order.core.model.OrderBusinessInfo;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static com.star.enterprise.order.base.exception.RestCode.CORE_OPERATE_NOT_ALLOW;

/**
 * @author xiaowenrou
 * @date 2023/4/19
 */
@Component
@AllArgsConstructor
public non-sealed class FrontEndCalculatorProvider extends CalculatorProcessorProvider {

    private final ApplicationContext context;

    @Override
    public CalculateProcessor doSupply() {
        return this.buildCommonProcessor();
    }

    @Override
    public CalculateProcessor doSupply(OrderFeeDetail detail) {
        var items = detail.items();
        var count = items.stream().map(OrderBusinessInfo::businessType)
                .filter(BusinessTypeEnum.WALLET.value()::equals).count();
        if (count > 0 && count != items.size()) {
            throw new BusinessWarnException(CORE_OPERATE_NOT_ALLOW, "error.order.conflictItem");
        }
        return count > 0 ? this.buildWalletProcessor() : this.buildCommonProcessor();
    }

    @Override
    public CalculateProcessor doSupplyWithExecute(OrderFeeDetail detail) {
        var items = detail.items();
        var count = items.stream().map(OrderBusinessInfo::businessType)
                .filter(BusinessTypeEnum.WALLET.value()::equals).count();
        if (count > 0 && count != items.size()) {
            throw new BusinessWarnException(CORE_OPERATE_NOT_ALLOW, "error.order.conflictItem");
        }
        return count > 0 ? this.buildWalletProcessorWithExecute() : this.buildCommonProcessorWithExecute();
    }

    private CalculateProcessor buildCommonProcessor() {
        var list = new ArrayList<CalculateProcessor>();
        list.add(this.context.getBean(TargetUserProcessor.class));
        list.add(this.context.getBean(DiscountPlanProcessor.class));
        list.add(this.context.getBean(DirectDiscountProcessor.class));
        list.add(this.context.getBean(OrderItemSaveProcessor.class));
        list.add(this.context.getBean(CouponProcessor.class));
        list.add(this.context.getBean(WalletProcessor.class));
        list.add(this.context.getBean(ChargeCategoryProcessor.class));
        return new CalculatorProcessorChain(list);
    }

    private CalculateProcessor buildCommonProcessorWithExecute() {
        var list = new ArrayList<CalculateProcessor>();
        list.add(this.context.getBean(TargetUserProcessor.class));
        list.add(this.context.getBean(DiscountPlanProcessor.class));
        list.add(this.context.getBean(DirectDiscountProcessor.class));
        list.add(this.context.getBean(OrderItemExecuteProcessor.class));
        list.add(this.context.getBean(CouponExecuteProcessor.class));
        list.add(this.context.getBean(WalletExecuteProcessor.class));
        list.add(this.context.getBean(ChargeCategoryExecuteProcessor.class));
        return new CalculatorProcessorChain(list);
    }

    private CalculateProcessor buildWalletProcessor() {
        var list = new ArrayList<CalculateProcessor>();
        list.add(this.context.getBean(TargetUserProcessor.class));
        list.add(this.context.getBean(OrderItemSaveProcessor.class));
        list.add(this.context.getBean(ChargeCategoryProcessor.class));
        return new CalculatorProcessorChain(list);
    }

    private CalculateProcessor buildWalletProcessorWithExecute() {
        var list = new ArrayList<CalculateProcessor>();
        list.add(this.context.getBean(TargetUserProcessor.class));
        list.add(this.context.getBean(OrderItemExecuteProcessor.class));
        list.add(this.context.getBean(ChargeCategoryExecuteProcessor.class));
        return new CalculatorProcessorChain(list);
    }


}
