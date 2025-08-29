package com.thirty.common.api.impl;

import com.thirty.common.api.SettingApi;
import com.thirty.common.enums.model.SettingField;
import com.thirty.common.service.basic.SettingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class SettingApiImpl implements SettingApi {
    @Resource
    private SettingService settingService;

    /**
     * 是否仅显示有权限操作的数据
     * @return 设置值
     */
    @Override
    public boolean hasPermissionDisplay() {
        return settingService.getBooleanSetting(SettingField.PERMISSION_DISPLAY);
    }
}
