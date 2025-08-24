package com.thirty.user.service.domain;

import com.thirty.user.model.vo.ViewVO;

import java.util.List;

public interface ViewQueryDomain {
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
