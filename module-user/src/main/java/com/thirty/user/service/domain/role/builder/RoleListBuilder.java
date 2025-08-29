package com.thirty.user.service.domain.role.builder;

import com.thirty.user.constant.RoleConstant;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.RoleVO;
import com.thirty.user.service.domain.role.RoleQueryDomain;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE) // 所有字段默认私有
public class RoleListBuilder {

    final RoleQueryDomain roleQueryDomain;

    public RoleListBuilder(RoleQueryDomain roleQueryDomain) {
        this.roleQueryDomain = roleQueryDomain;
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

    /**
     * 为用户查询
     */
    public RoleListBuilder forUser(Integer userId) {
        this.userId = userId;
        return this;
    }

    /**
     * 包含所有角色
     */
    public RoleListBuilder includeAllRoles() {
        this.includeAllRoles = true;
        return this;
    }

    /**
     * 包含用户当前角色
     */
    public RoleListBuilder includeUserRoles() {
        this.includeUserRoles = true;
        return this;
    }

    /**
     * 包含子角色
     */
    public RoleListBuilder includeChildRoles() {
        this.includeChildRoles = true;
        return this;
    }

    /**
     * 包含全局角色
     */
    public RoleListBuilder includeGlobalRoles() {
        this.includeGlobalRoles = true;
        return this;
    }

    /**
     * 构建角色列表
     */
    public List<Role> build() {
        Set<Role> resultRoles = new HashSet<>();

        if (includeAllRoles) {
            resultRoles.addAll(roleQueryDomain.getRoles());
        }
        if (includeUserRoles) {
            resultRoles.addAll(roleQueryDomain.getUserRoles(userId));
        }
        if (includeChildRoles) {
            resultRoles.addAll(roleQueryDomain.getChildRoles(userId));
        }
        if (includeGlobalRoles) {
            resultRoles.addAll(roleQueryDomain.getGlobalRoles());
        }

        return new ArrayList<>(resultRoles);
    }

    /**
     * 构建角色ID列表
     */
    public List<Integer> buildIds() {
        return Role.extractIds(build());
    }

    /**
     * 构建角色树（当前构建的角色列表作为有权限的角色）
     */
    public List<RoleVO> buildTree(boolean hasPermissionDisplay) {
        // 当前构建的角色ID列表作为有权限的角色
        List<Role> permittedRoles = build();
        List<Integer> permittedRoleIds = Role.extractIds(permittedRoles);

        // 所有角色
        List<Role> allRoles = roleQueryDomain.getRoles();

        // 确定要显示的角色
        List<Role> displayRoles = hasPermissionDisplay ? permittedRoles : allRoles;

        // 构建角色映射
        Map<Integer, RoleVO> roleMap = buildRoleMap(displayRoles, permittedRoleIds);

        // 构建角色树
        return buildRoleTree(displayRoles, roleMap);
    }

    /**
     * 构建角色树
     */
    private List<RoleVO> buildRoleTree(List<Role> roles, Map<Integer, RoleVO> roleMap) {
        List<RoleVO> roleTree = new ArrayList<>();
        roles.forEach(role -> {
            RoleVO currentVO = roleMap.get(role.getId());
            if (role.getParentNodeId().equals(RoleConstant.ROOT_ROLE_PARENT_ID)) {
                roleTree.add(currentVO);
            } else {
                RoleVO parentVO = roleMap.get(role.getParentNodeId());
                if (parentVO != null) {
                    parentVO.getChildren().add(currentVO);
                }
            }
        });
        return roleTree;
    }

    /**
     * 构建角色映射
     */
    private Map<Integer, RoleVO> buildRoleMap(List<Role> roles, List<Integer> permittedRoleIds) {
        Map<Integer, RoleVO> roleMap = new HashMap<>();
        roles.forEach(role -> {
            RoleVO roleVO = new RoleVO();
            roleVO.setNode(role);
            roleVO.setHasPermission(permittedRoleIds.contains(role.getId()));
            roleVO.setChildren(new ArrayList<>());
            roleMap.put(role.getId(), roleVO);
        });
        return roleMap;
    }
}
