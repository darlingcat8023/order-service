package com.star.enterprise.order.base.serialize.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.star.enterprise.order.base.utils.DateTimeUtils.DATE_TIME_FORMATTER;

/**
 * @author xiaowenrou
 * @date 2022/9/21
 */
public class LocalDateTimeValueSerializeImpl extends JsonSerializer<Long> {

    @Override
    public void serialize(Long aLong, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (aLong != null) {
            var localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(aLong), ZoneId.systemDefault());
            jsonGenerator.writeString(localDateTime.format(DATE_TIME_FORMATTER));
        } else {
            jsonGenerator.writeString((String) null);
        }

    }

}
