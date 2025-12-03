package com.thirty.user.service.domain.permission.bk;

import com.thirty.user.model.dto.PermissionBkDTO;

public interface PermissionBkOperationDomain {
    /**
     * 添加权限
     * @param permissionBkDTO 权限DTO
     */
    void addPermission(PermissionBkDTO permissionBkDTO);

    /**
     * 修改权限
     * @param permissionBkDTO 权限DTO
     */
    void modifyPermission(PermissionBkDTO permissionBkDTO);

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
