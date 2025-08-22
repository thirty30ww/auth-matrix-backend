package com.thirty.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.common.model.entity.Setting;
import com.thirty.common.service.SettingService;
import com.thirty.common.mapper.SettingMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【setting(全局设置表)】的数据库操作Service实现
* @createDate 2025-08-22 09:04:37
*/
@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting>
    implements SettingService{

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
}




