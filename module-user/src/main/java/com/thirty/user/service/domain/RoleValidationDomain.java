package com.thirty.user.service.domain;

import java.util.List;

public interface RoleValidationDomain {
    /**
     * 校验用户ID是否包含于当前用户的子角色
     * @param currentUserId 当前用户ID
     * @param targetUserId 目标用户ID
     * @return 是：true，否：false
     */
    boolean validateUserIdRolesContainChildRoles(Integer currentUserId, Integer targetUserId);

    /**
     * 校验用户列表中每个用户是否都是username用户的子用户
     * @param currentUserId 当前用户ID
     * @param targetUserIds 用户ID列表
     * @return 是：true，否：false
     */
    boolean validateUserIdsRolesContainChildRoles(Integer currentUserId, List<Integer> targetUserIds);

    /**
     * 验证角色列表是否都包含于子角色
     * @param currentUserId 当前用户ID
     * @param targetRoleIds 角色ID列表
     * @return 是：true，否：false
     */
    boolean validateRolesContainChildRoles(Integer currentUserId, List<Integer> targetRoleIds);
}
