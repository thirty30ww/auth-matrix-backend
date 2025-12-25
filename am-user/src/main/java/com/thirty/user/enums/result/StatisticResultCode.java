package com.thirty.user.enums.result;

import com.thirty.common.enums.IResult;
import com.thirty.common.enums.result.GlobalResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatisticResultCode implements IResult {
    GET_ONLINE_USERS_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取在线用户成功"),
    GET_CREATED_USER_COUNT_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取创建用户数量图表数据成功"),
    GET_LAST_TWO_DAY_CREATED_USER_COUNT_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取最近两天的用户创建数量成功"),
    GET_LAST_TWO_DAY_ABNORMAL_OPERATION_COUNT_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取最近两天的异常操作次数成功"),
    ;

    private final Integer code;
    private final String message;
}
