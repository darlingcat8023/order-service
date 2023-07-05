package com.star.enterprise.order.base.support;

import com.star.enterprise.order.base.exception.TaskExecuteWarnException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaowenrou
 * @date 2022/11/29
 */
@Slf4j
public abstract class EventMethodInvoker {

    /**
     * 包装对event的调用
     * @param runnable
     */
    public static void invoke(Runnable runnable) {
        try {
            runnable.run();
        } catch (TaskExecuteWarnException warn) {
            if (log.isWarnEnabled()) {
                log.warn(warn.getMessage());
            }
        } catch (Exception exp) {
            log.error(exp.getMessage(), exp);
        }
    }

}
