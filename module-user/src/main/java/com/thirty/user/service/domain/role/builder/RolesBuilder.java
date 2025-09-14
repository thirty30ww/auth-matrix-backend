package com.thirty.user.service.domain.role.builder;

import com.thirty.user.constant.RoleConstant;
import com.thirty.user.converter.RoleConverter;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.RoleVO;
import com.thirty.user.service.basic.RoleService;
import com.thirty.user.service.basic.UserRoleService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE) // 所有字段默认私有
public class RolesBuilder {

    final RoleService roleService;
    final UserRoleService userRoleService;

    public RolesBuilder(RoleService roleService, UserRoleService userRoleService) {
        this.roleService = roleService;
        this.userRoleService = userRoleService;
    }

    /**
     * 查询信息
     */
    Integer userId;

    /**
     * 查询状态
     */
    boolean includeAllRoles = false;
    boolean includeUserRoles = false;
    boolean includeChildRoles = false;
    boolean includeGlobalRoles = false;
    boolean excludeGlobalRoles = false;

    /**
     * 为用户查询
     */
    public RolesBuilder forUser(Integer userId) {
        this.userId = userId;
        return this;
    }

    /**
     * 包含所有角色
     */
    public RolesBuilder includeAllRoles() {
        this.includeAllRoles = true;
        return this;
    }

    /**
     * 所有角色但不包括全局角色
     */
    public RolesBuilder excludeGlobalRoles() {
        this.excludeGlobalRoles = true;
        return this;
    }

    /**
     * 包含用户当前角色
     */
    public RolesBuilder includeUserRoles() {
        this.includeUserRoles = true;
        return this;
    }

    /**
     * 包含子角色
     */
    public RolesBuilder includeChildRoles() {
        this.includeChildRoles = true;
        return this;
    }

    /**
     * 包含全局角色
     */
    public RolesBuilder includeGlobalRoles() {
        this.includeGlobalRoles = true;
        return this;
    }

    /**
     * 构建角色列表
     */
    public List<Role> build() {
        Set<Role> resultRoles = new HashSet<>();

        if (includeAllRoles) {
            resultRoles.addAll(roleService.list());
        }
        if (includeUserRoles) {
            resultRoles.addAll(userRoleService.getRolesByUserId(userId));
        }
        if (includeChildRoles) {
            List<Integer> roleIds = userRoleService.getRoleIds(userId);
            resultRoles.addAll(roleService.getDescendantRoles(roleIds));
        }
        if (excludeGlobalRoles) {
            resultRoles.addAll(roleService.getNotGlobalRoles());
        }
        if (includeGlobalRoles) {
            resultRoles.addAll(roleService.getChildRoles(RoleConstant.GLOBAL_ROLE_PARENT_ID));
        }

        return Role.sortByCreateTime(resultRoles);
    }

    /**
     * 构建角色ID列表
     */
    public List<Integer> buildIds() {
        return Role.extractIds(build());
    }

    /**
     * 构建角色树
     */
    public List<RoleVO> buildTree() {
        // 当前构建的角色ID列表作为基础角色列表
        List<Role> roles = build();
        List<RoleVO> roleVOS = RoleConverter.INSTANCE.toRoleVOS(roles);
        return buildRoleTree(roleVOS);
    }

    /**
     * 构建角色树（当前构建的角色列表作为有权限的角色）
     */
    public List<RoleVO> buildTree(boolean hasPermissionDisplay, List<Integer> permittedRoleIds) {
        // 当前构建的角色ID列表作为基础角色列表
        List<Role> roles = build();
        List<RoleVO> roleVOS = RoleConverter.INSTANCE.toRoleVOS(roles);

        if (hasPermissionDisplay) {
            RoleVO.filterHasPermission(permittedRoleIds, roleVOS);
        } else {
            RoleVO.setHasPermission(permittedRoleIds, roleVOS);
        }

        // 构建角色树
        return buildRoleTree(roleVOS);
    }

    /**
     * 构建角色树
     */
    private List<RoleVO> buildRoleTree(List<RoleVO> roleVOS) {
        List<RoleVO> roleTree = new ArrayList<>();
        Map<Integer, RoleVO> roleMap = RoleVO.buildMap(roleVOS);

        roleMap.forEach((roleId, roleVO) -> {
            Role currentRole = roleVO.getNode();
            if (currentRole.getParentNodeId().equals(RoleConstant.ROOT_ROLE_PARENT_ID)) {
                roleTree.add(roleVO);
            } else {
                RoleVO parentVO = roleMap.get(currentRole.getParentNodeId());
                if (parentVO != null) {
                    parentVO.getChildren().add(roleVO);
                } else {
                    roleTree.add(roleVO);
                }
            }
        });
        return roleTree;
    }
}
