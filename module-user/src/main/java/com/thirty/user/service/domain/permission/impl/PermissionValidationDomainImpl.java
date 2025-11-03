package com.thirty.user.service.domain.permission.impl;

import com.thirty.user.constant.PermissionConstant;
import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.model.entity.Permission;
import com.thirty.user.service.basic.PermissionService;
import com.thirty.user.service.basic.RoleService;
import com.thirty.user.service.basic.UserRoleService;
import com.thirty.user.service.domain.permission.PermissionQueryDomain;
import com.thirty.user.service.domain.permission.PermissionValidationDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class PermissionValidationDomainImpl implements PermissionValidationDomain {

    @Resource
    private PermissionQueryDomain permissionQueryDomain;

    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private PermissionService permissionService;

    /**
     * 校验用户是否有权限权限
     * @param userId 用户ID
     * @param permissionIds 权限ID列表
     * @return 是否有权限权限
     */
    @Override
    public boolean validateUserHavePermissions(Integer userId, List<Integer> permissionIds) {
        List<Integer> roleIds = userRoleService.getRoleIds(userId);
        roleIds.addAll(roleService.getDescendantRoleIds(roleIds));
        List<Integer> currentViewIds = permissionQueryDomain.getPermissionId(roleIds);
        return new HashSet<>(currentViewIds).containsAll(permissionIds);
    }

    /**
     * 校验用户是否有权限权限
     * @param userId 用户ID
     * @param permissionId 权限ID
     * @return 是否有权限权限
     */
    @Override
    public boolean validateUserHavePermission(Integer userId, Integer permissionId) {
        return validateUserHavePermissions(userId, List.of(permissionId));
    }

    /**
     * 校验类型是否符合
     * @param parentId 父节点ID
     * @param type 权限类型
     * @return 是否符合
     */
    @Override
    public boolean validateTypeComply(Integer parentId, PermissionType type) {
        Permission parentPermission = permissionService.getById(parentId);
        PermissionType parentType = parentPermission == null ? PermissionType.DIRECTORY : parentPermission.getType();

        if (type == PermissionType.DIRECTORY || type == PermissionType.MENU) {
            return parentType == PermissionType.DIRECTORY;
        } else {
            return parentType == PermissionType.MENU;
        }
    }

    /**
     * 校验父节点ID是否不等于权限ID及其后代节点ID
     * @param parentId 父节点ID
     * @param permissionId 权限ID
     * @return 是否不等于权限ID及其后代节点ID
     */
    @Override
    public boolean validateParentIdEqualsSelfAndDescendants(Integer parentId, Integer permissionId) {
        if (Objects.equals(parentId, permissionId)) {
            return true;
        }
        List<Integer> descendantIds = permissionService.getDescendantIds(parentId);
        return !CollectionUtils.isEmpty(descendantIds) && descendantIds.contains(permissionId);
    }

    /**
     * 校验是否可以上移
     * @param permissionId 权限ID
     * @return 是否可以上移
     */
    @Override
    public boolean validateMoveUp(Integer permissionId) {
        Permission permission = permissionService.getById(permissionId);
        return !Objects.equals(permission.getFrontNodeId(), PermissionConstant.HEAD_PERMISSION_FRONT_ID);
    }

    /**
     * 校验是否可以下移
     * @param permissionId 权限ID
     * @return 是否可以下移
     */
    @Override
    public boolean validateMoveDown(Integer permissionId) {
        Permission permission = permissionService.getById(permissionId);
        return !Objects.equals(permission.getBehindNodeId(), PermissionConstant.TAIL_PERMISSION_BEHIND_ID);
    }

    /**
     * 验证是否有权限修改权限的启用状态
     * @param userId 用户ID
     * @param permissionId 权限ID
     * @param isValid 权限状态
     * @return
     */
    @Override
    public boolean validateModifyValid(Integer userId, Integer permissionId, Boolean isValid) {
        Permission permission = permissionService.getById(permissionId);
        // 验证是否修改了权限的启用状态
        if (permission.getIsValid() == isValid) {
            return true;
        }
        // 如果对该权限的后代权限都有权限，就可以修改启用状态
        List<Integer> descendantIds = permissionService.getDescendantIds(permissionId);
        return validateUserHavePermissions(userId, descendantIds);
    }
}
