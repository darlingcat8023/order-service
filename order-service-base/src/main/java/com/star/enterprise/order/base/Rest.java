package com.star.enterprise.order.base;

/**
 * 标准响应体
 * @author xiaowenrou
 * @date 2022/9/14
 */
public record Rest<T>(int code, String message, T data) {

    public static <T> Rest<T> just(T data) {
        return just(0, "success", data);
    }

    public static <T> Rest<T> just(int code, String message, T data) {
        return new Rest<>(code, message, data);
    }

}
