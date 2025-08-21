package com.thirty.user.service;

import com.thirty.user.model.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【role(角色表)】的数据库操作Service
* @createDate 2025-08-09 15:40:41
*/
public interface RoleService extends IService<Role> {

    List<Role> getRoleList(String username, Boolean isChild);

    /**
     * 获取子角色列表
     * @param roles 当前用户的角色列表
     * @return 子角色列表
     */
    List<Role> getChildRoleList(List<Role> roles);
}
