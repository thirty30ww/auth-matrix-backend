package com.thirty.user.service.domain;

import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.RoleVO;

import java.util.List;

public interface RoleQueryDomain {
    /**
     * 获取角色列表
     * @return 角色列表
     */
    List<Role> getRoles();

    /**
     * 获取子角色列表
     * @param userId 用户ID
     * @return 子角色列表
     */
    List<Role> getChildRoles(Integer userId);

    /**
     * 获取子角色ID列表
     * @param userId 用户ID
     * @return 子角色ID列表
     */
    List<Integer> getChildRoleIds(Integer userId);

    /**
     * 获取角色VO列表
     * @param userId 用户ID
     * @param hasPermissionDisplay 是否仅显示有权限操作的角色
     * @return 角色VO列表
     */
    List<RoleVO> getRoleTree(Integer userId, boolean hasPermissionDisplay);

    /**
     * 获取全局角色列表
     * @return 全局角色列表
     */
    List<Role> getGlobalRoles();
}
