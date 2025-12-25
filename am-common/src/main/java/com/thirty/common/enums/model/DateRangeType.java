package com.thirty.common.enums.model;

import com.thirty.common.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DateRangeType implements CodeEnum<Integer> {
    TODAY(1),
    THIS_WEEK(2),
    THIS_MONTH(3),
    THIS_YEAR(4),
    ;

    private final Integer code;
}
