package com.thirty.user.service.facade;

import com.thirty.user.model.dto.ViewDTO;
import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;

import java.util.List;

public interface ViewFacade {
    /**
     * 获取视图树
     * @param userId 用户ID
     * @return 视图树
     */
    List<ViewVO> getViewTree(Integer userId);

    /**
     * 获取菜单树
     * @param userId 用户ID
     * @return 菜单树
     */
    List<ViewVO> getMenuTree(Integer userId);

    /**
     * 获取菜单和按钮树
     * @param userId 用户ID
     * @return 菜单和按钮树
     */
    List<ViewVO> getMenuAndButtonTree(Integer userId, Integer targetRoleId);

    /**
     * 获取目录树
     * @param userId 用户ID
     * @return 目录树
     */
    List<ViewVO> getDirectoryTree(Integer userId);

    /**
     * 获取视图列表
     * @param keyword 视图名称关键词
     * @return 视图列表
     */
    List<View> getViews(String keyword);

    /**
     * 获取权限码列表
     * @param userId 用户ID
     * @return 权限码列表
     */
    List<String> getPermissionCode(Integer userId);

    /**
     * 添加视图
     * @param userId 用户ID
     * @param viewDTO 视图DTO
     */
    void addView(Integer userId, ViewDTO viewDTO);

    /**
     * 修改视图
     * @param userId 用户ID
     * @param viewDTO 视图DTO
     */
    void modifyView(Integer userId, ViewDTO viewDTO);

    /**
     * 删除视图
     * @param userId 用户ID
     * @param viewId 视图ID
     */
    void deleteView(Integer userId, Integer viewId);

    /**
     * 移动视图
     * @param viewId 视图ID
     * @param isUp 是否上移
     */
    void moveView(Integer viewId, Boolean isUp);
}
