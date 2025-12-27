package com.thirty.user.enums.result;

import com.thirty.common.enums.IResult;
import com.thirty.common.enums.result.GlobalResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeResultCode implements IResult {
    /**
     * 获取通知列表成功
     */
    GET_NOTICE_LIST_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取通知列表成功"),
    ;

    private final Integer code;
    private final String message;

}
