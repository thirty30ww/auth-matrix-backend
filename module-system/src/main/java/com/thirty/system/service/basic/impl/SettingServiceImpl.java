package com.thirty.system.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.common.utils.TypeUtil;
import com.thirty.system.converter.SettingConverter;
import com.thirty.system.enums.model.SettingField;
import com.thirty.system.enums.model.SettingType;
import com.thirty.system.mapper.SettingMapper;
import com.thirty.system.model.dto.SettingDTO;
import com.thirty.system.model.entity.Setting;
import com.thirty.system.model.vo.SettingVO;
import com.thirty.system.service.basic.SettingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author Lenovo
* @description 针对表【setting(全局设置表)】的数据库操作Service实现
* @createDate 2025-08-22 09:04:37
*/
@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting>
    implements SettingService {

    @Resource
    private TypeUtil typeUtil;

    /**
     * 获取设置值
     * @param settingField 设置字段
     * @return 设置值
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getSettingValue(SettingField settingField) {
        Setting setting = getSettingByField(settingField.getCode());
        SettingType settingType = settingField.getValueType();
        return (T) typeUtil.convertValue(setting.getValue(), settingType.getMainType(), settingType.getElementType());
    }

    /**
     * 获取公共设置值
     * @param settingField 设置字段
     * @return 设置值
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getPublicSettingValue(SettingField settingField) {
        Setting setting = getPublicSettingByField(settingField.getCode());
        SettingType settingType = settingField.getValueType();
        return (T) typeUtil.convertValue(setting.getValue(), settingType.getMainType(), settingType.getElementType());
    }

    /**
     * 获取所有设置值
     * @return 设置值
     */
    @Override
    public List<SettingVO> getSettingVOS() {
        List<Setting> settings = list();
        List<SettingVO> settingVOS = SettingConverter.INSTANCE.toSettingVOS(settings);

        // 转换值类型
        settingVOS.forEach(settingVO ->{
            SettingType type = SettingField.getValueType(settingVO.getField());
            settingVO.setValue(typeUtil.convertValue(settingVO.getValue(), type.getMainType(), type.getElementType()));
        });
        
        return settingVOS;
    }

    /**
     * 根据字段名获取设置
     * @param field 字段名
     * @return 设置
     */
    @Override
    public Setting getSettingByField(String field) {
        QueryWrapper<Setting> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("field", field);
        return getOne(queryWrapper);
    }

    /**
     * 根据字段名获取公共设置
     * @param field 字段名
     * @return 设置
     */
    @Override
    public Setting getPublicSettingByField(String field) {
        QueryWrapper<Setting> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("field", field);
        Setting setting = getOne(queryWrapper);
        return SettingField.isPublic(setting.getField()) ? setting : null;
    }


    /**
     * 修改设置
     * @param settingDTOS 设置DTO列表
     */
    @Override
    public void modifySettings(List<SettingDTO> settingDTOS) {
        List<Integer> settingIds = SettingDTO.extractIds(settingDTOS);  // 提取ID列表
        List<Setting> settings = listByIds(settingIds); // 根据ID列表查询设置实体
        Map<Integer, Setting> settingMap = Setting.buildMap(settings);  // 构建ID到设置实体的映射

        // 转换DTO值为字符串并更新实体值
        settingDTOS.forEach(settingDTO -> {
            Setting setting = settingMap.get(settingDTO.getId());
            SettingField settingField = SettingField.getByCode(setting.getField()); // 获取设置字段枚举
            SettingType settingType = settingField.getValueType();
            
            // 使用带类型校验的转换方法
            setting.setValue(typeUtil.convertToStringWithValidation(settingDTO.getValue(), settingType.getMainType(), settingType.getElementType()));
        });
        updateBatchById(settings);
    }

}




