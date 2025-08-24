package com.thirty.user.service.domain;

import java.util.List;

public interface RoleValidationDomain {
    /**
     * 校验用户列表中每个用户是否都是username用户的子用户
     * @param userIds 用户ID列表
     * @return 是：true，否：false
     */
    boolean validateUserIdsRolesContainChildRoles(String username, List<Integer> userIds);

    /**
     * 验证角色列表是否都包含于子角色
     * @param username 用户名
     * @param roleIds 角色ID列表
     * @return 是：true，否：false
     */
    boolean validateRolesContainChildRoles(String username, List<Integer> roleIds);
}
