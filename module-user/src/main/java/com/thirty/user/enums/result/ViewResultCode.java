package com.thirty.user.enums.result;

import com.thirty.common.enums.IResult;
import com.thirty.common.enums.result.GlobalResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ViewResultCode implements IResult {

    GET_TREE_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取目录树成功"),
    GET_LIST_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取视图列表成功"),
    ;

    private final Integer code;
    private final String message;
}
