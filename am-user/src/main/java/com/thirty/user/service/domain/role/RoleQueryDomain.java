package com.thirty.user.service.domain.role;

import com.thirty.user.model.entity.Role;

import java.util.List;

public interface RoleQueryDomain {
    /**
     * 获取角色列表
     * @return 角色列表
     */
    List<Role> getRoles();

    /**
     * 根据用户ID查询角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Integer> getRoleIds(Integer userId);
}
