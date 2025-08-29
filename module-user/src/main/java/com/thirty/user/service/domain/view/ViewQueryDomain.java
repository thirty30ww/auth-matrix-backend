package com.thirty.user.service.domain.view;

import com.thirty.user.model.vo.ViewVO;

import java.util.List;

public interface ViewQueryDomain {
    /**
     * 获取菜单树
     * @param currentRoleIds 当前角色id列表
     * @param targetRoleId 目标角色id
     * @return 菜单树
     */
    List<ViewVO> getMenuTree(List<Integer> currentRoleIds, Integer targetRoleId);

    /**
     * 获取菜单树
     * @param currentRoleIds 当前角色id列表
     * @return 菜单树
     */
    List<ViewVO> getMenuTree(List<Integer> currentRoleIds);

    /**
     * 获取菜单树
     * @return 菜单树
     */
    List<ViewVO> getMenuTree();

    /**
     * 获取视图树
     * @return 视图树
     */
    List<ViewVO> getViewTree();
}
