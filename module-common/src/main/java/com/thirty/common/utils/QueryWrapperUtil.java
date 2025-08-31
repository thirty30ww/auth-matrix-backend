package com.thirty.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 查询包装工具类
 */
public class QueryWrapperUtil {

    public static <T> QueryWrapper<T> eqSafe(QueryWrapper<T> wrapper, String column, Object value) {
        return wrapper.eq(value != null, column, value);
    }

    public static <T> QueryWrapper<T> inSafe(QueryWrapper<T> wrapper, String column, List<?> values) {
        return wrapper.in(CollectionUtils.isEmpty(values) && !values.isEmpty(), column, values);
    }

    public static <T> QueryWrapper<T> likeSafe(QueryWrapper<T> wrapper, String column, String value) {
        return wrapper.like(value != null, column, value);
    }
}
