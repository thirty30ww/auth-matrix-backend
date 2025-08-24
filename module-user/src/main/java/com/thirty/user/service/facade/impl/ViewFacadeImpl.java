package com.thirty.user.service.facade.impl;

import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;
import com.thirty.user.service.basic.ViewService;
import com.thirty.user.service.domain.ViewQueryDomain;
import com.thirty.user.service.facade.ViewFacade;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewFacadeImpl implements ViewFacade {
    @Resource
    private ViewService viewService;
    @Resource
    private ViewQueryDomain viewQueryDomain;

    /**
     * 获取视图树
     * @param onlyMenu 是否只获取菜单
     * @return 视图树
     */
    @Override
    public List<ViewVO> getViewTree(boolean onlyMenu) {
        return onlyMenu ? viewQueryDomain.getMenuTree() : viewQueryDomain.getViewTree();
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
