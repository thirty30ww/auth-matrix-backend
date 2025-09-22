package com.thirty.user.service.facade;

import com.thirty.user.model.dto.PermissionDTO;
import com.thirty.user.model.vo.PermissionVO;

import java.util.List;

public interface PermissionFacade {
    /**
     * 获取权限树
     * @param userId 用户ID
     * @return 权限树
     */
    List<PermissionVO> getViewTree(Integer userId);

    /**
     * 获取菜单树
     * @param userId 用户ID
     * @return 菜单树
     */
    List<PermissionVO> getMenuTree(Integer userId);

    /**
     * 获取菜单和按钮树
     * @param userId 用户ID
     * @return 菜单和按钮树
     */
    List<PermissionVO> getMenuAndButtonTree(Integer userId, Integer targetRoleId);

    /**
     * 获取目录树
     * @param userId 用户ID
     * @return 目录树
     */
    List<PermissionVO> getDirectoryTree(Integer userId);

    /**
     * 获取权限列表
     * @param keyword 权限名称关键词
     * @return 权限列表
     */
    List<PermissionVO> getViewVOS(Integer userId, String keyword);

    /**
     * 获取权限码列表
     * @param userId 用户ID
     * @return 权限码列表
     */
    List<String> getPermissionCode(Integer userId);

    /**
     * 添加权限
     * @param userId 用户ID
     * @param permissionDTO 权限DTO
     */
    void addPermission(Integer userId, PermissionDTO permissionDTO);

    /**
     * 修改权限
     * @param userId 用户ID
     * @param permissionDTO 权限DTO
     */
    void modifyPermission(Integer userId, PermissionDTO permissionDTO);

    /**
     * 删除权限
     * @param userId 用户ID
     * @param viewId 权限ID
     */
    void deletePermission(Integer userId, Integer viewId);

    /**
     * 移动权限
     * @param viewId 权限ID
     * @param isUp 是否上移
     */
    void movePermission(Integer viewId, Boolean isUp);
}
