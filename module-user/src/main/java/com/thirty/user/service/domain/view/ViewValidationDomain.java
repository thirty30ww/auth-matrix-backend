package com.thirty.user.service.domain.view;

import com.thirty.user.enums.model.ViewType;

import java.util.List;

public interface ViewValidationDomain {
    /**
     * 校验用户是否有视图权限
     * @param userId 用户ID
     * @param userIds 视图ID列表
     * @return 是否有视图权限
     */
    boolean validateViewContainUserViews(Integer userId, List<Integer> userIds);

    /**
     * 校验用户是否有视图权限
     * @param userId 用户ID
     * @param viewId 视图ID
     * @return 是否有视图权限
     */
    boolean validateViewContainUserViews(Integer userId, Integer viewId);

    /**
     * 校验类型是否符合
     * @param parentId 父节点ID
     * @param type 视图类型
     * @return 是否符合
     */
    boolean validateTypeComply(Integer parentId, ViewType type);

    /**
     * 校验父节点ID是否不等于视图ID及其后代节点ID
     * @param parentId 父节点ID
     * @param viewId 视图ID
     * @return 是否不等于视图ID及其后代节点ID
     */
    boolean validateParentIdEqualsSelfAndDescendants(Integer parentId, Integer viewId);

    /**
     * 校验是否可以上移
     * @param viewId 视图ID
     * @return 是否可以上移
     */
    boolean validateMoveUp(Integer viewId);

    /**
     * 校验是否可以下移
     * @param viewId 视图ID
     * @return 是否可以下移
     */
    boolean validateMoveDown(Integer viewId);

    /**
     * 校验视图是否可以修改状态
     * @param userId 用户ID
     * @param viewId 视图ID
     * @param isValid 视图状态
     * @return 是否可以修改状态
     */
    boolean validateModifyValid(Integer userId, Integer viewId, Boolean isValid);
}
