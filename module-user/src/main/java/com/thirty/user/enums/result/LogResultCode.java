package com.thirty.user.enums.result;


import com.thirty.common.enums.IResult;
import com.thirty.common.enums.result.GlobalResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LogResultCode implements IResult {
    GET_OPERATE_LOG_LIST_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取操作日志列表成功"),
    GET_OPERATE_LOG_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取操作日志详情成功"),
    GET_OPERATE_LOG_CODE_LIST_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取操作日志代码列表成功"),
    GET_OPERATE_LOG_MODULE_LIST_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取操作日志模块列表成功"),
    ;
    private final Integer code;
    private final String message;
}
