package com.star.enterprise.order.base.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.InputStream;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;

/**
 * 静态json工具类
 * @author xiaowenrou
 * @date 2022/8/24
 */
public abstract class Jackson extends ObjectMapper {

    private static final Jackson MAPPER = new Jackson() {};

    private Jackson() {
        this.setAnnotationIntrospector(new RecordJacksonAnnotationIntrospector());
        this.configure(ALLOW_UNQUOTED_FIELD_NAMES, true);
        this.configure(FAIL_ON_EMPTY_BEANS, false);
        this.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.setSerializationInclusion(NON_NULL);
    }

    /**
     * Json -> Class
     * @param content
     * @param valueType
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> T read(String content, Class<T> valueType) {
        return MAPPER.readValue(content,valueType);
    }

    /**
     * Json -> Object
     * @param content
     * @param valueTypeRef
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> T read(String content, TypeReference<T> valueTypeRef) {
        return MAPPER.readValue(content,valueTypeRef);
    }

    /**
     * Json -> Class
     * @param src
     * @param valueType
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> T read(InputStream src, Class<T> valueType) {
        return MAPPER.readValue(src,valueType);
    }

    /**
     * Json -> Object
     * @param src
     * @param valueTypeRef
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> T read(InputStream src, TypeReference<T> valueTypeRef) {
        return MAPPER.readValue(src, valueTypeRef);
    }

    /**
     * Json -> Class
     * @param src
     * @param valueType
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> T read(byte[] src, Class<T> valueType) {
        return MAPPER.readValue(src, valueType);
    }

    /**
     * Json -> Object
     * @param src
     * @param valueTypeRef
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> T read(byte[] src, TypeReference<T> valueTypeRef) {
        return MAPPER.readValue(src, valueTypeRef);
    }

    /**
     * Object -> Json
     * @param value
     * @return
     */
    @SneakyThrows
    public static String writeString(Object value) {
        return MAPPER.writeValueAsString(value);
    }

    /**
     * Object -> Json(byte)
     * @param value
     * @return
     */
    @SneakyThrows
    public static byte[] writeBytes(Object value) {
        return MAPPER.writeValueAsBytes(value);
    }

}
