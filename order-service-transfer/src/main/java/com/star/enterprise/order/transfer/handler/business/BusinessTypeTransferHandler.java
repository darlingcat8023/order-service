package com.star.enterprise.order.transfer.handler.business;

import com.star.enterprise.order.charge.constants.BusinessTypeEnum;
import com.star.enterprise.order.charge.model.TargetUser;
import com.star.enterprise.order.transfer.calculator.TransferAccumulateHolder;
import com.star.enterprise.order.transfer.model.OrderTransferItemInfo;
import org.springframework.context.ApplicationContext;

/**
 * @author xiaowenrou
 * @date 2022/11/4
 */
public abstract class BusinessTypeTransferHandler {

    /**
     * 获取处理器
     * @param type
     * @param context
     * @return
     */
    public static BusinessTypeTransferHandler createHandler(BusinessTypeEnum type, final ApplicationContext context) {
        var clazz = switch (type) {
            case COURSE -> CourseBusinessTransferHandler.class;
            case ARTICLE -> ArticleBusinessTransferHandler.class;
            case MATCH -> MatchBusinessTransferHandler.class;
            case WALLET -> WalletBusinessTransferHandler.class;
        };
        return context.getBean(clazz);
    }

    /**
     * 处理订单业务数据
     * @param targetUser
     * @param item
     * @param holder
     */
    public void processItemBusinessInfo(TargetUser targetUser, OrderTransferItemInfo item, TransferAccumulateHolder holder) {}

    /**
     * 触发订单收费之后的业务处理
     * @param transferOrderId
     * @param target
     * @param item
     * @param holder
     */
    public void processItemAfterSaved(String transferOrderId, TargetUser target, OrderTransferItemInfo item, TransferAccumulateHolder holder) {}

}
