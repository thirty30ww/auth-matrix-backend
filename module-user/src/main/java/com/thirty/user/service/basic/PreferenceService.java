package com.thirty.user.service.basic;

import com.baomidou.mybatisplus.extension.service.IService;
import com.thirty.user.model.entity.Preference;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【preference(用户偏好设置)】的数据库操作Service
* @createDate 2025-09-13 19:58:16
*/
public interface PreferenceService extends IService<Preference> {
    /**
     * 获取用户偏好设置
     * @param userId 用户ID
     * @return 用户偏好设置列表
     */
    List<Preference> getPreferences(Integer userId);

    /**
     * 保存用户偏好设置
     * @param userId 用户ID
     * @param field 偏好字段名
     * @param value 偏好值
     */
    void savePreference(Integer userId, String field, String value);
}
