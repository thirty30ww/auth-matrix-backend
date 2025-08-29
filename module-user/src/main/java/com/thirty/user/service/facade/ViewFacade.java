package com.thirty.user.service.facade;

import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;

import java.util.List;

public interface ViewFacade {
    /**
     * 获取视图树
     * @return 视图树
     */
    List<ViewVO> getViewTree();

    /**
     * 获取菜单树
     * @param userId 用户ID
     * @param targetRoleId 目标角色ID
     * @return 菜单树
     */
    List<ViewVO> getMenuTree(Integer userId, Integer targetRoleId);

    /**
     * 获取视图列表
     * @param keyword 视图名称关键词
     * @return 视图列表
     */
    List<View> getViews(String keyword);
}
