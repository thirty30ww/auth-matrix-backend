package com.thirty.user.service.facade;

import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;

import java.util.List;

public interface ViewFacade {
    /**
     * 获取视图树
     * @param onlyMenu 是否只获取菜单
     * @return 视图树
     */
    List<ViewVO> getViewTree(boolean onlyMenu);

    /**
     * 获取视图列表
     * @param keyword 视图名称关键词
     * @return 视图列表
     */
    List<View> getViews(String keyword);
}
