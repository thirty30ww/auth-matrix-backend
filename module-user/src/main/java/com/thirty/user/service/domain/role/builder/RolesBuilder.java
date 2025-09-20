package com.thirty.user.service.domain.role.builder;

import com.thirty.user.constant.RoleConstant;
import com.thirty.user.converter.RoleConverter;
import com.thirty.user.enums.model.RoleType;
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
    List<RoleType> roleTypes;

    /**
     * 为用户查询
     */
    public RolesBuilder forUser(Integer userId) {
        this.userId = userId;
        return this;
    }

    /**
     * 角色类型列表查询
     */
    public RolesBuilder forRoleTypes(List<RoleType> roleTypes) {
        this.roleTypes = roleTypes;
        return this;
    }

    /**
     * 角色类型查询
     */
    public RolesBuilder forRoleType(RoleType roleType) {
        this.roleTypes = List.of(roleType);
        return this;
    }

    /**
     * 构建角色列表
     */
    public List<Role> build() {
        Set<Role> resultRoles = new HashSet<>();

        roleTypes.forEach(rolesType -> {
            switch (rolesType) {
                case ALL:
                    resultRoles.addAll(roleService.list());
                    break;
                case CHILD:
                    List<Integer> roleIds = userRoleService.getRoleIds(userId);
                    resultRoles.addAll(roleService.getDescendantRoles(roleIds));
                    break;
                case GLOBAL:
                    resultRoles.addAll(roleService.getChildRoles(RoleConstant.GLOBAL_ROLE_PARENT_ID));
                    break;
                case SELF:
                    resultRoles.addAll(userRoleService.getRolesByUserId(userId));
                    break;
                case NOT_GLOBAL:
                    resultRoles.addAll(roleService.getNotGlobalRoles());
                    break;
            }
        });

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
