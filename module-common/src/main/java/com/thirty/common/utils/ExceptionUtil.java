package com.thirty.common.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExceptionUtil {

    /**
     * 根据字段在类中的声明顺序获取第一个字段错误
     */
    public static FieldError getFirstFieldErrorByOrder(List<FieldError> fieldErrors, Object target) {
        if (fieldErrors.size() == 1) {
            return fieldErrors.get(0);
        }

        // 获取目标类的字段声明顺序
        Class<?> targetClass = target.getClass();
        List<String> fieldOrder = Arrays.stream(targetClass.getDeclaredFields())
                .map(Field::getName)
                .toList();

        // 创建字段名到顺序的映射
        Map<String, Integer> fieldIndexMap = fieldOrder.stream()
                .collect(Collectors.toMap(
                        fieldName -> fieldName,
                        fieldOrder::indexOf
                ));

        // 按照字段声明顺序排序并返回第一个
        return fieldErrors.stream()
                .filter(error -> fieldIndexMap.containsKey(error.getField()))
                .min(Comparator.comparing(error -> fieldIndexMap.get(error.getField())))
                .orElse(fieldErrors.get(0)); // 如果找不到匹配的字段，返回原始第一个
    }
}
