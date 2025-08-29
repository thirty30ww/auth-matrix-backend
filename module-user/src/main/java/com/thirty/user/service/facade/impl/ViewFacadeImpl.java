package com.thirty.user.service.facade.impl;

import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;
import com.thirty.user.service.basic.ViewService;
import com.thirty.user.service.domain.role.RoleQueryDomain;
import com.thirty.user.service.domain.view.ViewQueryDomain;
import com.thirty.user.service.facade.ViewFacade;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewFacadeImpl implements ViewFacade {
    @Resource
    private ViewService viewService;

    @Resource
    private RoleQueryDomain roleQueryDomain;
    @Resource
    private ViewQueryDomain viewQueryDomain;

    /**
     * 获取视图树
     * @return 视图树
     */
    @Override
    public List<ViewVO> getViewTree() {
        return viewQueryDomain.getViewTree();
    }

    /**
     * 获取菜单树
     * @param userId 用户ID
     * @param targetRoleId 目标角色ID
     * @return 菜单树
     */
    @Override
    public List<ViewVO> getMenuTree(Integer userId, Integer targetRoleId) {
        List<Integer> currentRoleIds = roleQueryDomain.getRoleIds(userId);
        return targetRoleId == null ? viewQueryDomain.getMenuTree(currentRoleIds) : viewQueryDomain.getMenuTree(currentRoleIds, targetRoleId);
    }

    /**
     * 获取视图列表
     * @param keyword 视图名称关键词
     * @return 视图列表
     */
    @Override
    public List<View> getViews(String keyword) {
        return viewService.getNotDirectoryViews(keyword);
    }
}
