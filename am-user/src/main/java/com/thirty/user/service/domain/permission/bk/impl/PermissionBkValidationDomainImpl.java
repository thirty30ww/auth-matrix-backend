package com.thirty.user.service.domain.permission.bk.impl;

import com.thirty.user.constant.PermissionConstant;
import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.service.basic.PermissionBkService;
import com.thirty.user.service.basic.RoleService;
import com.thirty.user.service.basic.UserRoleService;
import com.thirty.user.service.domain.permission.bk.PermissionBkQueryDomain;
import com.thirty.user.service.domain.permission.bk.PermissionBkValidationDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class PermissionBkValidationDomainImpl implements PermissionBkValidationDomain {

    @Resource
    private PermissionBkQueryDomain permissionBkQueryDomain;

    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private PermissionBkService permissionBkService;

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
        List<Integer> currentViewIds = permissionBkQueryDomain.getPermissionId(roleIds);
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
        PermissionBk parentPermissionBk = permissionBkService.getById(parentId);
        PermissionType parentType = parentPermissionBk == null ? PermissionType.DIRECTORY : parentPermissionBk.getType();

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
        List<Integer> descendantIds = permissionBkService.getDescendantIds(parentId);
        return !CollectionUtils.isEmpty(descendantIds) && descendantIds.contains(permissionId);
    }

    /**
     * 校验是否可以上移
     * @param permissionId 权限ID
     * @return 是否可以上移
     */
    @Override
    public boolean validateMoveUp(Integer permissionId) {
        PermissionBk permissionBk = permissionBkService.getById(permissionId);
        return !Objects.equals(permissionBk.getFrontId(), PermissionConstant.HEAD_PERMISSION_FRONT_ID);
    }

    /**
     * 校验是否可以下移
     * @param permissionId 权限ID
     * @return 是否可以下移
     */
    @Override
    public boolean validateMoveDown(Integer permissionId) {
        PermissionBk permissionBk = permissionBkService.getById(permissionId);
        return !Objects.equals(permissionBk.getBehindId(), PermissionConstant.TAIL_PERMISSION_BEHIND_ID);
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
        PermissionBk permissionBk = permissionBkService.getById(permissionId);
        // 验证是否修改了权限的启用状态
        if (permissionBk.getIsValid() == isValid) {
            return true;
        }
        // 如果对该权限的后代权限都有权限，就可以修改启用状态
        List<Integer> descendantIds = permissionBkService.getDescendantIds(permissionId);
        return validateUserHavePermissions(userId, descendantIds);
    }
}
