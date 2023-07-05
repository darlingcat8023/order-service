package com.star.enterprise.order.remote;

import feign.codec.ErrorDecoder;

import java.lang.annotation.*;

/**
 * @author xiaowenrou
 * @date 2023/3/22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface UseErrorDecoder {

    /**
     * 使用替换的异常解码器
     * @return
     */
    Class<? extends ErrorDecoder> value();

}
