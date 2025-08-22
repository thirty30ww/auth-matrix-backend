package com.thirty.user.service;

import com.thirty.user.model.entity.View;
import com.baomidou.mybatisplus.extension.service.IService;
import com.thirty.user.model.vo.ViewVO;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【view(页面表)】的数据库操作Service
* @createDate 2025-08-03 10:06:39
*/
public interface ViewService extends IService<View> {
    /**
     * 获取页面树
     * @param onlyMenu 是否仅获取菜单
     * @return 页面树
     */
    List<ViewVO> getViewTree(boolean onlyMenu);

    /**
     * 获取页面列表
     * @param keyword 搜索关键词
     * @return 页面列表
     */
    List<View> getViewList(String keyword);
}
