package com.thirty.system.service.basic;

import com.thirty.system.enums.model.SettingField;
import com.thirty.system.model.entity.Setting;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Lenovo
* @description 针对表【setting(全局设置表)】的数据库操作Service
* @createDate 2025-08-22 09:04:37
*/
public interface SettingService extends IService<Setting> {
    /**
     * 获取布尔类型的设置
     * @param settingField 设置字段
     * @return 设置值
     */
    boolean getBooleanSetting(SettingField settingField);

    /**
     * 根据字段名获取设置
     * @param field 字段名
     * @return 设置
     */
    Setting getSettingByField(String field);
}
