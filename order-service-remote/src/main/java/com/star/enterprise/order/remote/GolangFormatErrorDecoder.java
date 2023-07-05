package com.star.enterprise.order.remote;

import com.fasterxml.jackson.databind.JsonNode;
import com.star.enterprise.order.base.utils.Jackson;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author xiaowenrou
 * @date 2023/3/22
 */
public class GolangFormatErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        try (var bodyStream = response.body().asInputStream()) {
            var node = Jackson.read(bodyStream, JsonNode.class);
            var message = node.get("message").asText("");
            if (!StringUtils.hasText(message)) {
                message = node.get("err").asText("remote service fail");
            }
            return new RemoteServiceBreakerException(message);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
    }

}
