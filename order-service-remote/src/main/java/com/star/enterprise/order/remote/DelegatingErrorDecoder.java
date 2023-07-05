package com.star.enterprise.order.remote;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author xiaowenrou
 * @date 2023/3/22
 */
@AllArgsConstructor
public class DelegatingErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder delegate;

    @Override
    public Exception decode(String methodKey, Response response) {
        var metaData = response.request().requestTemplate().methodMetadata();
        var decoderAnnotation = AnnotationUtils.findAnnotation(metaData.method(), UseErrorDecoder.class);
        if (decoderAnnotation == null) {
            decoderAnnotation = AnnotationUtils.findAnnotation(metaData.method().getDeclaringClass(), UseErrorDecoder.class);
        }
        if (decoderAnnotation == null) {
            return this.delegate.decode(methodKey, response);
        } else {
            return BeanUtils.instantiateClass(decoderAnnotation.value()).decode(methodKey, response);
        }
    }


}
