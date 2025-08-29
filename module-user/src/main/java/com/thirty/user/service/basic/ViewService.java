package com.thirty.user.service.basic;

import com.thirty.user.model.entity.View;
import com.baomidou.mybatisplus.extension.service.IService;
import com.thirty.user.model.vo.ViewVO;

import java.util.List;
import java.util.Map;

/**
* @author Lenovo
* @description 针对表【view(页面表)】的数据库操作Service
* @createDate 2025-08-03 10:06:39
*/
public interface ViewService extends IService<View> {
    /**
     * 获取视图列表（不包含DIRECTORY）
     * @param keyword 视图名称
     * @return 视图列表
     */
    List<View> getNotDirectoryViews(String keyword);

    /**
     * 获取菜单列表（不包含PAGE）
     * @return 菜单列表
     */
    List<View> getNotPageViews();

    /**
     * 获取非页面视图Map，key为视图ID，value为ViewVO
     * @return 非页面视图Map
     */
    Map<Integer, ViewVO> getNotPageViewVOMap();

    /**
     * 获取页面列表（仅包含PAGE）
     * @return 页面列表
     */
    List<View> getPageViews();

    /**
     * 获取页面VO列表
     * @return 页面VO列表
     */
    List<ViewVO> getPageViewVOS();
}
