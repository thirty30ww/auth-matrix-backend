package com.thirty.user.service.basic;

import com.thirty.user.model.entity.RoleView;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【role_view(角色页面表)】的数据库操作Service
* @createDate 2025-08-22 14:21:16
*/
public interface RoleViewService extends IService<RoleView> {
    /**
     * 获取视图id列表
     * @param roleId 角色id
     * @return 视图id列表
     */
    List<Integer> getViewIds(Integer roleId);

    /**
     * 获取视图id列表
     * @param roleIds 角色id列表
     * @return 视图id列表
     */
    List<Integer> getViewIds(List<Integer> roleIds);
}
