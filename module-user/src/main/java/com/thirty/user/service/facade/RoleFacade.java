package com.thirty.user.service.facade;

import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.RoleVO;

import java.util.List;

public interface RoleFacade {

    /**
     * 获取角色VO列表
     * @param currentUserId 当前用户ID
     * @return 角色VO列表
     */
    List<RoleVO> getRoleTree(Integer currentUserId);

    /**
     * 获取角色列表
     * @param currentUserId 当前用户ID
     * @param isChild 是否仅获取子角色
     * @return 角色列表
     */
    List<Role> getRoles(Integer currentUserId, boolean isChild);

    /**
     * 获取全局子角色列表
     * @return 全局子角色列表
     */
    List<Role> getGlobalRoles();
}
