package com.star.enterprise.order.base.serialize;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.star.enterprise.order.base.serialize.support.LocalDateTimeValueSerializeImpl;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author xiaowenrou
 * @date 2022/9/21
 */
@Documented
@Target(value = {FIELD})
@Retention(value = RUNTIME)
@JsonSerialize(using = LocalDateTimeValueSerializeImpl.class)
@JacksonAnnotationsInside
public @interface LocalDateTimeValue {
}
