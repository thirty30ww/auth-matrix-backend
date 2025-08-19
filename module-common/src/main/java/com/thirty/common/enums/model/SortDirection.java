package com.thirty.common.enums.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.thirty.common.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortDirection implements CodeEnum<String> {
    ASC("asc", "升序"),
    DESC("desc", "降序");

    @JsonValue  // JSON序列化时返回这个值
    private final String code;
    private final String description;
}
