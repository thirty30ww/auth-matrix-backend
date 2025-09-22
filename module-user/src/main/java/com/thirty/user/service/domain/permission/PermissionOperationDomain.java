package com.thirty.user.service.domain.permission;

import com.thirty.user.model.dto.PermissionDTO;

public interface PermissionOperationDomain {
    /**
     * 添加权限
     * @param permissionDTO 权限DTO
     */
    void addPermission(PermissionDTO permissionDTO);

    /**
     * 修改权限
     * @param permissionDTO 权限DTO
     */
    void modifyPermission(PermissionDTO permissionDTO);

    /**
     * 删除权限
     * @param viewId 权限ID
     */
    void deletePermission(Integer viewId);

    /**
     * 移动权限
     * @param viewId 权限ID
     * @param isUp 是否上移
     */
    void movePermission(Integer viewId, Boolean isUp);
}
