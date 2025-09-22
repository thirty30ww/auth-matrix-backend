package com.thirty.user.service.domain.permission;

import com.thirty.user.enums.model.PermissionType;

import java.util.List;

public interface PermissionValidationDomain {
    /**
     * 校验用户是否有权限权限
     * @param userId 用户ID
     * @param userIds 权限ID列表
     * @return 是否有权限权限
     */
    boolean validateViewContainUserViews(Integer userId, List<Integer> userIds);

    /**
     * 校验用户是否有权限权限
     * @param userId 用户ID
     * @param viewId 权限ID
     * @return 是否有权限权限
     */
    boolean validateViewContainUserViews(Integer userId, Integer viewId);

    /**
     * 校验类型是否符合
     * @param parentId 父节点ID
     * @param type 权限类型
     * @return 是否符合
     */
    boolean validateTypeComply(Integer parentId, PermissionType type);

    /**
     * 校验父节点ID是否不等于权限ID及其后代节点ID
     * @param parentId 父节点ID
     * @param viewId 权限ID
     * @return 是否不等于权限ID及其后代节点ID
     */
    boolean validateParentIdEqualsSelfAndDescendants(Integer parentId, Integer viewId);

    /**
     * 校验是否可以上移
     * @param viewId 权限ID
     * @return 是否可以上移
     */
    boolean validateMoveUp(Integer viewId);

    /**
     * 校验是否可以下移
     * @param viewId 权限ID
     * @return 是否可以下移
     */
    boolean validateMoveDown(Integer viewId);

    /**
     * 校验权限是否可以修改状态
     * @param userId 用户ID
     * @param viewId 权限ID
     * @param isValid 权限状态
     * @return 是否可以修改状态
     */
    boolean validateModifyValid(Integer userId, Integer viewId, Boolean isValid);
}
