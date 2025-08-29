package com.thirty.common.converter;

import com.thirty.common.enums.CodeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * String 到 CodeEnum 的转换器工厂
 * 专门处理数字代码到枚举的转换
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

    private record StringToCodeEnumConverter<T extends Enum<T> & CodeEnum<Integer>>(Class<T> enumType)
            implements Converter<String, T> {

        @Override
        public T convert(String source) {
            // 尝试按数字代码转换
            Integer code = Integer.parseInt(source);
            T result = CodeEnum.getByCode(enumType, code);
            if (result != null) {
                return result;
            }

            // 尝试按枚举名称转换
            return Enum.valueOf(enumType, source);
        }
    }
}