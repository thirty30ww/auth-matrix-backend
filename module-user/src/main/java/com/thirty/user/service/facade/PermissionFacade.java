package com.thirty.user.service.facade;

import com.thirty.user.model.dto.PermissionDTO;
import com.thirty.user.model.vo.PermissionVO;

import java.util.List;

public interface PermissionFacade {
    /**
     * 获取视图树
     * @param userId 用户ID
     * @return 视图树
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
     * 获取视图列表
     * @param keyword 视图名称关键词
     * @return 视图列表
     */
    List<PermissionVO> getViewVOS(Integer userId, String keyword);

    /**
     * 获取权限码列表
     * @param userId 用户ID
     * @return 权限码列表
     */
    List<String> getPermissionCode(Integer userId);

    /**
     * 添加视图
     * @param userId 用户ID
     * @param permissionDTO 视图DTO
     */
    void addPermission(Integer userId, PermissionDTO permissionDTO);

    /**
     * 修改视图
     * @param userId 用户ID
     * @param permissionDTO 视图DTO
     */
    void modifyPermission(Integer userId, PermissionDTO permissionDTO);

    /**
     * 删除视图
     * @param userId 用户ID
     * @param viewId 视图ID
     */
    void deletePermission(Integer userId, Integer viewId);

    /**
     * 移动视图
     * @param viewId 视图ID
     * @param isUp 是否上移
     */
    void movePermission(Integer viewId, Boolean isUp);
}
