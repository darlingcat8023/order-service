package com.star.enterprise.order.remote;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static feign.Util.decodeOrDefault;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author xiaowenrou
 * @data 2022/6/22
 */
public class FeignInfoLevelLogger extends Logger {

    private final org.slf4j.Logger logger;

    public FeignInfoLevelLogger() {
        this(Logger.class);
    }

    public FeignInfoLevelLogger(Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

    public FeignInfoLevelLogger(String name) {
        this(LoggerFactory.getLogger(name));
    }

    FeignInfoLevelLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        if (this.logger.isInfoEnabled()) {
            super.logRequest(configKey, logLevel, request);
        }
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        return this.logger.isInfoEnabled() ? this.logAndRebufferResponseWithoutHeader(configKey, logLevel, response, elapsedTime) : response;
    }

    /**
     * 屏蔽响应头
     * @param configKey
     * @param logLevel
     * @param response
     * @param elapsedTime
     * @return
     * @throws IOException
     */
    private Response logAndRebufferResponseWithoutHeader(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        String reason = response.reason() != null && logLevel.compareTo(Level.NONE) > 0 ? " " + response.reason() : "";
        int status = response.status();
        log(configKey, "<--- HTTP/1.1 %s%s (%sms)", status, reason, elapsedTime);
        if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {
            int bodyLength = 0;
            if (response.body() != null && !(status == 204 || status == 205)) {
                byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                bodyLength = bodyData.length;
                if (logLevel.ordinal() >= Level.FULL.ordinal() && bodyLength > 0) {
                    log(configKey, "%s", decodeOrDefault(bodyData, UTF_8, "Binary data"));
                }
                log(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
                return response.toBuilder().body(bodyData).build();
            } else {
                log(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
            }
        }
        return response;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        if (this.logger.isInfoEnabled()) {
            this.logger.info(String.format(methodTag(configKey) + format, args));
        }
    }

}
