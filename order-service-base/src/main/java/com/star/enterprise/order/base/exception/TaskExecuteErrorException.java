package com.star.enterprise.order.base.exception;

/**
 * @author xiaowenrou
 * @date 2022/11/29
 */
public class TaskExecuteErrorException extends RuntimeException {

    public TaskExecuteErrorException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
