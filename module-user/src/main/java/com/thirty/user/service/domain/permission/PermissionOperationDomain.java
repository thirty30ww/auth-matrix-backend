package com.thirty.user.service.domain.permission;

import com.thirty.user.model.dto.PermissionDTO;

public interface PermissionOperationDomain {
    /**
     * 添加视图
     * @param permissionDTO 视图DTO
     */
    void addPermission(PermissionDTO permissionDTO);

    /**
     * 修改视图
     * @param permissionDTO 视图DTO
     */
    void modifyPermission(PermissionDTO permissionDTO);

    /**
     * 删除视图
     * @param viewId 视图ID
     */
    void deletePermission(Integer viewId);

    /**
     * 移动视图
     * @param viewId 视图ID
     * @param isUp 是否上移
     */
    void movePermission(Integer viewId, Boolean isUp);
}
