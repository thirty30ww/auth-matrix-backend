package com.thirty.user.service.facade;

import com.thirty.user.enums.model.RoleListType;
import com.thirty.user.model.dto.AssignViewDTO;
import com.thirty.user.model.dto.RoleDTO;
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
     * @param type 类型
     * @return 角色列表
     */
    List<Role> getRoles(Integer currentUserId, RoleListType type);

    /**
     * 添加角色
     * @param roleDTO 角色dto
     */
    void addRole(RoleDTO roleDTO, Integer userId);

    /**
     * 更新角色
     * @param roleDTO 角色dto
     * @param userId 用户ID
     */
    void updateRole(RoleDTO roleDTO, Integer userId);

    /**
     * 删除角色
     * @param roleId 角色ID
     * @param userId 用户ID
     */
    void deleteRole(Integer roleId, Integer userId);

    /**
     * 分配视图权限
     * @param userId 当前操作用户ID
     * @param assignViewDTO 分配视图dto
     */
    void assignView(Integer userId, AssignViewDTO assignViewDTO);
}
