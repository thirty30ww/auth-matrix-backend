package com.thirty.user.service.domain.view.impl;

import com.thirty.user.constant.ViewConstant;
import com.thirty.user.enums.model.ViewType;
import com.thirty.user.model.entity.View;
import com.thirty.user.service.basic.UserRoleService;
import com.thirty.user.service.basic.ViewService;
import com.thirty.user.service.domain.view.ViewQueryDomain;
import com.thirty.user.service.domain.view.ViewValidationDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class ViewValidationDomainImpl implements ViewValidationDomain {

    @Resource
    private ViewQueryDomain viewQueryDomain;

    @Resource
    private UserRoleService userRoleService;
    @Resource
    private ViewService viewService;

    /**
     * 校验用户是否有视图权限
     * @param userId 用户ID
     * @param viewIds 视图ID列表
     * @return 是否有视图权限
     */
    @Override
    public boolean validateViewContainUserViews(Integer userId, List<Integer> viewIds) {
        List<Integer> currentUserRoleIds = userRoleService.getRoleIds(userId);
        List<Integer> currentViewIds = viewQueryDomain.getPermissionId(currentUserRoleIds);
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
    public boolean validateTypeComply(Integer parentId, ViewType type) {
        ViewType parentType = viewService.getById(parentId).getType();
        if (type == ViewType.DIRECTORY || type == ViewType.MENU) {
            return parentType == ViewType.DIRECTORY || Objects.equals(parentId, ViewConstant.ROOT_VIEW_PARENT_ID);
        } else {
            return parentType == ViewType.MENU;
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
        List<Integer> descendantIds = viewService.getDescendantIds(parentId);
        return !CollectionUtils.isEmpty(descendantIds) && descendantIds.contains(viewId);
    }

    /**
     * 校验是否可以上移
     * @param viewId 视图ID
     * @return 是否可以上移
     */
    @Override
    public boolean validateMoveUp(Integer viewId) {
        View view = viewService.getById(viewId);
        return !Objects.equals(view.getFrontNodeId(), ViewConstant.HEAD_VIEW_FRONT_ID);
    }

    /**
     * 校验是否可以下移
     * @param viewId 视图ID
     * @return 是否可以下移
     */
    @Override
    public boolean validateMoveDown(Integer viewId) {
        View view = viewService.getById(viewId);
        return !Objects.equals(view.getBehindNodeId(), ViewConstant.TAIL_VIEW_BEHIND_ID);
    }
}
