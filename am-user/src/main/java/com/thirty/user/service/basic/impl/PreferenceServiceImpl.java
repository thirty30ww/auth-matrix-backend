package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.mapper.PreferenceMapper;
import com.thirty.user.model.entity.Preference;
import com.thirty.user.service.basic.PreferenceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【preference(用户偏好设置)】的数据库操作Service实现
* @createDate 2025-09-13 19:58:16
*/
@Service
public class PreferenceServiceImpl extends ServiceImpl<PreferenceMapper, Preference>
    implements PreferenceService{

    /**
     * 获取用户偏好设置
     * @param userId 用户ID
     * @return 用户偏好设置列表
     */
    @Override
    public List<Preference> getPreferences(Integer userId) {
        LambdaQueryWrapper<Preference> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Preference::getUserId, userId);
        return list(queryWrapper);
    }

    /**
     * 保存用户偏好设置
     * @param userId 用户ID
     * @param field 偏好字段名
     * @param value 偏好值
     */
    @Override
    public void savePreference(Integer userId, String field, String value) {
        Preference preference = new Preference(userId, field, value);
        LambdaQueryWrapper<Preference> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Preference::getUserId, userId).eq(Preference::getField, field);
        saveOrUpdate(preference, queryWrapper);
    }
}