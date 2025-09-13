package com.thirty.common.utils;

import org.springframework.stereotype.Component;

@Component
public class TypeUtil {

    @SuppressWarnings("unchecked")
    public <T> T convertValue(Object value, Class<T> targetType) {
        if (targetType == Boolean.class) {
            return (T) Boolean.valueOf("1".equals(value));
        } else if (targetType == Integer.class) {
            return (T) Integer.valueOf(value.toString());
        } else {
            return (T) value.toString();
        }
    }
}
