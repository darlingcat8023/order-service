package com.star.enterprise.order.refund.handler.business;

import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.refund.calculator.RefundAccumulateHolder;
import com.star.enterprise.order.refund.model.OrderItemDelegate;
import com.star.enterprise.order.refund.model.OrderRefundItemInfo;
import org.springframework.context.ApplicationContext;

/**
 * @author xiaowenrou
 * @date 2022/11/4
 */
public abstract class BusinessTypeRefundHandler {

    /**
     * 获取处理器
     * @param type
     * @param context
     * @return
     */
    public static BusinessTypeRefundHandler createHandler(BusinessTypeEnum type, final ApplicationContext context) {
        var clazz = switch (type) {
            case COURSE -> CourseBusinessRefundHandler.class;
            case ARTICLE -> ArticleBusinessRefundHandler.class;
            case MATCH -> MatchBusinessRefundHandler.class;
            case WALLET -> WalletBusinessRefundHandler.class;
        };
        return context.getBean(clazz);
    }

    public void processItemBeforePredicate(TargetUser targetUser, OrderItemDelegate item) {

    }

    /**
     * 处理订单业务数据
     * @param targetUser
     * @param item
     * @param holder
     */
    public void processItemBusinessInfo(TargetUser targetUser, OrderRefundItemInfo item, RefundAccumulateHolder holder) {

    }

    /**
     * 触发订单收费之后的业务处理
     * @param targetUser
     * @param item
     * @param holder
     */
    public void processItemAfterSaved(String refundOrderId, TargetUser targetUser, OrderRefundItemInfo item, RefundAccumulateHolder holder) {}

}
