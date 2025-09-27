package com.thirty.system.enums.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.thirty.common.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SettingField implements CodeEnum<String> {
    /**
     * 是否仅显示有权限操作的数据
     */
    PERMISSION_DISPLAY("permission_display", Boolean.class),
    /**
     * 项目标题
     */
    PROJECT_TITLE("project_title", String.class, true),
    /**
     * 用户角色数量限制
     */
    USER_ROLE_NUMBER_LIMIT("user_role_number_limit", Integer.class),
    ;

    @EnumValue
    private final String code;

    /**
     * 值类型
     */
    private final Class<?> valueType;

    /**
     * 是否为公共设置
     */
    private final Boolean isPublic;

    /**
     * 私有构造函数，默认非公共设置
     * @param code 编码
     */
    SettingField(String code, Class<?> valueType) {
        this(code, valueType, false);
    }

    /**
     * 根据编码获取枚举值
     * @param code 编码
     * @return 枚举值
     */
    public static SettingField getByCode(String code) {
        return CodeEnum.getByCode(SettingField.class, code);
    }

    /**
     * 根据编码获取值类型
     * @param code 编码
     * @return 值类型
     */
    public static Class<?> getValueType(String code) {
        SettingField field = getByCode(code);
        return field == null ? String.class : field.getValueType();
    }

    /**
     * 判断设置是否为公共设置
     * @param code 编码
     * @return 是否为公共设置
     */
    public static boolean isPublic(String code) {
        return getByCode(code).getIsPublic();
    }
}
