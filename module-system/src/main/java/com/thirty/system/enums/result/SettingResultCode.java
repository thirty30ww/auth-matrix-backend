package com.thirty.system.enums.result;

import com.thirty.common.enums.IResult;
import com.thirty.common.enums.result.GlobalResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SettingResultCode implements IResult {
    GET_SETTING_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获得设置成功"),
    GET_SETTING_VALUES_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获得所有设置值成功"),
    MODIFY_SETTINGS_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "修改设置成功"),

    ;

    private final Integer code;
    private final String message;
}
