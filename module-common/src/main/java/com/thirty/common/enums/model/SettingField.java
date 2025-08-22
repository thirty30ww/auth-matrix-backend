package com.thirty.common.enums.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.thirty.common.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SettingField implements CodeEnum<String> {
    /**
     * 是否仅显示有权限操作的数据（1：是，0：否）
     */
    PERMISSION_DISPLAY("permission_display"),
    ;

    @EnumValue
    private final String code;
}
