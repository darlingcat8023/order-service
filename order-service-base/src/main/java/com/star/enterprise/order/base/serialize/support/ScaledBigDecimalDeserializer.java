package com.star.enterprise.order.base.serialize.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @author xiaowenrou
 * @date 2022/9/27
 */
@AllArgsConstructor
public class ScaledBigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    private final int scale;

    private final RoundingMode mode;

    @Override
    public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if (Objects.isNull(jsonParser.getDecimalValue())) {
            return null;
        } else {
            // 这里取floor
            return jsonParser.getDecimalValue().setScale(this.scale, this.mode);
        }
    }

}
