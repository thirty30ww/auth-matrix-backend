package com.thirty.system.service.basic;

import com.baomidou.mybatisplus.extension.service.IService;
import com.thirty.system.enums.model.SettingField;
import com.thirty.system.model.dto.SettingDTO;
import com.thirty.system.model.entity.Setting;
import com.thirty.system.model.vo.SettingVO;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【setting(全局设置表)】的数据库操作Service
* @createDate 2025-08-22 09:04:37
*/
public interface SettingService extends IService<Setting> {

    /**
     * 获取设置值
     * @param settingField 设置字段
     * @return 设置值
     */
    <T> T getSettingValue(SettingField settingField);

    /**
     * 获取公共设置值
     * @param settingField 设置字段
     * @return 设置值
     */
    <T> T getPublicSettingValue(SettingField settingField);

    /**
     * 获取所有设置值
     * @return 设置值
     */
    List<SettingVO> getSettingVOS();

    /**
     * 根据字段名获取设置
     * @param field 字段名
     * @return 设置
     */
    Setting getSettingByField(String field);

    /**
     * 根据字段名获取公共设置
     * @param field 字段名
     * @return 设置
     */
    Setting getPublicSettingByField(String field);

    /**
     * 修改设置
     * @param settingDTOS 设置DTO列表
     */
    void modifySettings(List<SettingDTO> settingDTOS);
}
