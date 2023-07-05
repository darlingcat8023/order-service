package com.star.enterprise.order.core.handler.business;

import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.matcher.MatchResult;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.core.calculator.OrderItemFeeDetail;
import com.star.enterprise.order.core.calculator.holder.DelegatingAccumulateHolder;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * @author xiaowenrou
 * @date 2022/11/4
 */
public abstract class BusinessTypeHandler {

    /**
     * 获取处理器
     * @param type
     * @param context
     * @return
     */
    public static BusinessTypeHandler createHandler(BusinessTypeEnum type, final ApplicationContext context) {
        var clazz = switch (type) {
            case COURSE -> CourseBusinessHandler.class;
            case ARTICLE -> ArticleBusinessHandler.class;
            case MATCH -> MatchBusinessHandler.class;
            case WALLET -> WalletBusinessHandler.class;
        };
        return context.getBean(clazz);
    }

    /**
     * 处理订单业务数据
     * @param targetUser
     * @param item
     */
    public void processItemBusinessInfo(TargetUser targetUser, OrderItemFeeDetail item, DelegatingAccumulateHolder holder) {
        var opt = item.operator();
        opt.setAfterDiscountSinglePrice(opt.getOriginalSinglePrice());
        opt.setAfterDiscountTotalPrice(opt.getOriginalTotalPrice());
        opt.setDueCollectSinglePrice(opt.getAfterDiscountSinglePrice());
        opt.setDueCollectPrice(opt.getAfterDiscountTotalPrice());
    }

    /**
     * 触发订单收费之后的业务处理
     * @param targetUser
     * @param item
     * @param holder
     */
    public void processItemAfterSaved(String orderId, TargetUser targetUser, OrderItemFeeDetail item, DelegatingAccumulateHolder holder) {}

    /**
     * 获取收费类型的匹配器
     * @param target
     * @param item
     * @return
     */
    public List<MatchResult> calculateChargeMatchResult(TargetUser target, OrderItemFeeDetail item) {
        return List.of();
    }

    /**
     * 处理收费类型更新
     * @param orderId
     * @param itemId
     * @param target
     * @param source
     * @param object
     */
    public void modifyChargeCategory(String orderId, String itemId, TargetUser target, MatchResult source, MatchResult object) {}

}
