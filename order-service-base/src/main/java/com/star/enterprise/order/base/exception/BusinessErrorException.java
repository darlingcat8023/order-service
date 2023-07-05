package com.star.enterprise.order.base.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaowenrou
 * @date 2022/9/14
 */
@Slf4j
public class BusinessErrorException extends WebBasedException {

    @Getter
    public final String messageKey;

    @Getter
    public final Object[] vars;

    public BusinessErrorException(RestCode code, String messageKey, Object ... vars) {
        super(code, null);
        this.messageKey = messageKey;
        this.vars = vars;
    }

    public BusinessErrorException(RestCode code, String messageKey, Throwable throwable, Object ... vars) {
        super(code, null, throwable);
        this.messageKey = messageKey;
        this.vars = vars;
    }

}
