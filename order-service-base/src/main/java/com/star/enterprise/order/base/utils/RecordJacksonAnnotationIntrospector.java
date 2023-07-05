package com.star.enterprise.order.base.utils;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * @author xiaowenrou
 * @date 2022/12/7
 */
public class RecordJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public PropertyName findNameForDeserialization(Annotated a) {
        var nameForDeserialization = super.findNameForDeserialization(a);
        if (PropertyName.USE_DEFAULT.equals(nameForDeserialization) && a instanceof AnnotatedParameter && ((AnnotatedParameter) a).getDeclaringClass().isRecord()) {
            var str = findImplicitPropertyName((AnnotatedParameter) a);
            if (str != null && !str.isEmpty()) {
                return PropertyName.construct(str);
            }
        }
        return nameForDeserialization;
    }

    @Override
    public String findImplicitPropertyName(AnnotatedMember m) {
        if (m.getDeclaringClass().isRecord()) {
            if (m instanceof AnnotatedParameter parameter) {
                return m.getDeclaringClass().getRecordComponents()[parameter.getIndex()].getName();
            }
            for (var recordComponent : m.getDeclaringClass().getRecordComponents()) {
                if (recordComponent.getName().equals(m.getName())) {
                    return m.getName();
                }
            }
        }
        return super.findImplicitPropertyName(m);
    }

}
