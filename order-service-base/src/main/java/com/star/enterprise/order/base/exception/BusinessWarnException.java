package com.star.enterprise.order.base.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaowenrou
 * @date 2022/9/14
 */
@Slf4j
public class BusinessWarnException extends WebBasedException {

    @Getter
    private final String messageKey;

    @Getter
    private final Object[] vars;

    public BusinessWarnException(RestCode code, String messageKey, Object ... vars) {
        super(code, null);
        this.messageKey = messageKey;
        this.vars = vars;
    }

    public BusinessWarnException(RestCode code, String messageKey, Throwable throwable, Object ... vars) {
        super(code, null, throwable);
        this.messageKey = messageKey;
        this.vars = vars;
    }

}
