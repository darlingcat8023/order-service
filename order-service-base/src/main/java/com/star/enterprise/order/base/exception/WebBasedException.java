package com.star.enterprise.order.base.exception;

import lombok.Getter;

/**
 * @author xiaowenrou
 * @date 2022/9/14
 */
public abstract class WebBasedException extends RuntimeException {

    @Getter
    private final int code;

    WebBasedException(RestCode code, String message) {
        this(code, message, null);
    }

    WebBasedException(RestCode code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code.getCode();
    }

}
