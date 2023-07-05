package com.star.enterprise.order.base.serialize.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author xiaowenrou
 * @date 2022/9/27
 */
@AllArgsConstructor
public class ScaledBigDecimalSerializer extends JsonSerializer<BigDecimal> {

    private final int scale;

    private final RoundingMode mode;


    @Override
    public void serialize(BigDecimal decimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (decimal == null) {
            jsonGenerator.writeNull();
        } else {
            // 这里取floor
            jsonGenerator.writeNumber(decimal.setScale(this.scale, this.mode));
        }
    }

}
