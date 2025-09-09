package com.thirty.user.service.domain.view;

import java.util.List;

public interface ViewQueryDomain {

    /**
     * 获取菜单id列表
     * @param roleId 角色id
     * @return 菜单id列表
     */
    List<Integer> getPermissionId(Integer roleId);

    /**
     * 获取菜单id列表
     * @param roleIds 角色id列表
     * @return 菜单id列表
     */
    List<Integer> getPermissionId(List<Integer> roleIds);

    /**
     * 获取权限码列表
     * @param roleIds 角色id列表
     * @return 权限码列表
     */
    List<String> getPermissionCode(List<Integer> roleIds);
}
