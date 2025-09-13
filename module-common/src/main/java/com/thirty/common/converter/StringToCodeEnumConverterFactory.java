package com.thirty.common.converter;

import com.thirty.common.enums.CodeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * String 到 CodeEnum 的转换器工厂
 * 支持数字代码和字符串代码到枚举的转换
 */
@SuppressWarnings({"rawtypes", "unchecked", "NullableProblems"})
public class StringToCodeEnumConverterFactory implements ConverterFactory<String, Enum> {

    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        if (CodeEnum.class.isAssignableFrom(targetType)) {
            return new StringToCodeEnumConverter(targetType);
        }
        return null;
    }

    private record StringToCodeEnumConverter<T extends Enum<T> & CodeEnum>(Class<T> enumType)
            implements Converter<String, T> {

        @Override
        public T convert(String source) {
            // 获取枚举的所有实例进行匹配
            T[] enumConstants = enumType.getEnumConstants();

            // 首先尝试直接按Code匹配（支持String类型的Code）
            for (T enumValue : enumConstants) {
                if (source.equals(String.valueOf(enumValue.getCode()))) {
                    return enumValue;
                }
            }

            // 尝试将输入解析为Integer，然后按Code匹配（支持Integer类型的Code）
            try {
                Integer intCode = Integer.parseInt(source);
                for (T enumValue : enumConstants) {
                    if (intCode.equals(enumValue.getCode())) {
                        return enumValue;
                    }
                }
            } catch (Exception ignored) {
            }

            // 最后尝试按枚举名称转换
            return Enum.valueOf(enumType, source);
        }
    }
}