package com.thirty.user.service.domain.permission.impl;

import com.thirty.user.constant.ViewConstant;
import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.model.entity.Permission;
import com.thirty.user.service.basic.UserRoleService;
import com.thirty.user.service.basic.PermissionService;
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
    private UserRoleService userRoleService;
    @Resource
    private PermissionService permissionService;

    /**
     * 校验用户是否有视图权限
     * @param userId 用户ID
     * @param viewIds 视图ID列表
     * @return 是否有视图权限
     */
    @Override
    public boolean validateViewContainUserViews(Integer userId, List<Integer> viewIds) {
        List<Integer> currentUserRoleIds = userRoleService.getRoleIds(userId);
        List<Integer> currentViewIds = permissionQueryDomain.getPermissionId(currentUserRoleIds);
        return new HashSet<>(currentViewIds).containsAll(viewIds);
    }

    /**
     * 校验用户是否有视图权限
     * @param userId 用户ID
     * @param viewId 视图ID
     * @return 是否有视图权限
     */
    @Override
    public boolean validateViewContainUserViews(Integer userId, Integer viewId) {
        return validateViewContainUserViews(userId, List.of(viewId));
    }

    /**
     * 校验类型是否符合
     * @param parentId 父节点ID
     * @param type 视图类型
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
     * 校验父节点ID是否不等于视图ID及其后代节点ID
     * @param parentId 父节点ID
     * @param viewId 视图ID
     * @return 是否不等于视图ID及其后代节点ID
     */
    @Override
    public boolean validateParentIdEqualsSelfAndDescendants(Integer parentId, Integer viewId) {
        if (Objects.equals(parentId, viewId)) {
            return true;
        }
        List<Integer> descendantIds = permissionService.getDescendantIds(parentId);
        return !CollectionUtils.isEmpty(descendantIds) && descendantIds.contains(viewId);
    }

    /**
     * 校验是否可以上移
     * @param viewId 视图ID
     * @return 是否可以上移
     */
    @Override
    public boolean validateMoveUp(Integer viewId) {
        Permission permission = permissionService.getById(viewId);
        return !Objects.equals(permission.getFrontNodeId(), ViewConstant.HEAD_VIEW_FRONT_ID);
    }

    /**
     * 校验是否可以下移
     * @param viewId 视图ID
     * @return 是否可以下移
     */
    @Override
    public boolean validateMoveDown(Integer viewId) {
        Permission permission = permissionService.getById(viewId);
        return !Objects.equals(permission.getBehindNodeId(), ViewConstant.TAIL_VIEW_BEHIND_ID);
    }

    /**
     * 校验视图是否可以修改状态
     * @param userId 用户ID
     * @param viewId 视图ID
     * @param isValid 视图状态
     * @return 是否可以修改状态
     */
    @Override
    public boolean validateModifyValid(Integer userId, Integer viewId, Boolean isValid) {
        Permission permission = permissionService.getById(viewId);
        if (permission.getIsValid() == isValid) {
            return true;
        }
        List<Integer> descendantIds = permissionService.getDescendantIds(viewId);
        return validateViewContainUserViews(userId, descendantIds);
    }
}
