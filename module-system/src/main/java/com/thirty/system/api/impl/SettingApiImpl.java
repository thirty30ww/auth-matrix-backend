package com.thirty.system.api.impl;

import com.thirty.system.api.SettingApi;
import com.thirty.system.enums.model.SettingField;
import com.thirty.system.service.basic.SettingService;
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
        return settingService.getSettingValue(SettingField.PERMISSION_DISPLAY);
    }

     /**
     * 获取限制的用户角色数量
     * @return 限制的用户角色数量
     */
     @Override
     public Integer getUserRoleNumberLimit() {
        return settingService.getSettingValue(SettingField.USER_ROLE_NUMBER_LIMIT);
    }
}
