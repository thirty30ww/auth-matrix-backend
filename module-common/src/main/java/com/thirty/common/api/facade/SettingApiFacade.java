package com.thirty.common.api.facade;

import com.thirty.common.api.SettingApi;
import com.thirty.common.enums.model.SettingField;
import com.thirty.common.model.entity.Setting;
import com.thirty.common.service.SettingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class SettingApiFacade implements SettingApi {
    @Resource
    private SettingService settingService;

    /**
     * 是否仅显示有权限操作的数据
     * @return 设置值
     */
    @Override
    public boolean isPermissionDisplay() {
        return getBooleanSetting(SettingField.PERMISSION_DISPLAY);
    }

    /**
     * 获取布尔类型设置
     * @param settingField 设置字段枚举
     * @return 设置值
     */
    private boolean getBooleanSetting(SettingField settingField) {
        Setting setting = settingService.getSettingByField(settingField.getCode());
        return setting.getValue() == 1;
    }
}
