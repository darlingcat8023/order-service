package com.star.enterprise.order.remote;

import feign.Response;
import feign.codec.ErrorDecoder;

/**
 * @author xiaowenrou
 * @date 2023/3/22
 */
public class DefaultErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        return this.defaultDecoder.decode(methodKey, response);
    }

}
