package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.converter.ViewConverter;
import com.thirty.user.enums.model.ViewType;
import com.thirty.user.mapper.ViewMapper;
import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;
import com.thirty.user.service.basic.ViewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author Lenovo
* @description 针对表【view(页面表)】的数据库操作Service实现
* @createDate 2025-08-03 10:06:39
*/
@Service
public class ViewServiceImpl extends ServiceImpl<ViewMapper, View>
    implements ViewService{

    /**
     * 获取视图列表（不包含DIRECTORY）
     * @param keyword 视图名称
     * @return 视图列表
     */
    @Override
    public List<View> getNotDirectoryViews(String keyword) {
        QueryWrapper<View> wrapper = new QueryWrapper<>();
        wrapper.like("name", keyword)
                .ne("type", ViewType.DIRECTORY); // 筛选不为目录的节点
        return list(wrapper);
    }

    /**
     * 获取菜单列表（不包含PAGE）
     * @return 菜单列表
     */
    @Override
    public List<View> getNotPageViews() {
        QueryWrapper<View> menuWrapper = new QueryWrapper<>();
        menuWrapper.ne("type", ViewType.PAGE);    // 筛选不为页面的节点
        return list(menuWrapper);
    }

    /**
     * 获取非页面视图Map，key为视图ID，value为View
     * @return 非页面视图Map
     */
    @Override
    public Map<Integer, ViewVO> getNotPageViewVOMap() {
        List<View> notPageViews = getNotPageViews();
        return ViewConverter.INSTANCE.toViewVOMap(notPageViews);
    }

    /**
     * 获取页面列表（仅包含PAGE）
     * @return 页面列表
     */
    @Override
    public List<View> getPageViews() {
        QueryWrapper<View> pageWrapper = new QueryWrapper<>();
        pageWrapper.eq("type", ViewType.PAGE);    // 筛选为页面的节点
        return list(pageWrapper);
    }

    /**
     * 获取页面VO列表
     * @return 页面VO列表
     */
    @Override
    public List<ViewVO> getPageViewVOS() {
        List<View> pageViews = getPageViews();
        return ViewConverter.INSTANCE.toViewVOS(pageViews);
    }
}




