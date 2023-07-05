package com.star.enterprise.order.remote;

/**
 * @author xiaowenrou
 * @date 2023/1/31
 */
public class RemoteServiceBreakerException extends RuntimeException {

    public RemoteServiceBreakerException(String message) {
        this(message, null);
    }

    public RemoteServiceBreakerException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
