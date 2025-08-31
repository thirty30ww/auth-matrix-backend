package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.constant.RoleConstant;
import com.thirty.user.converter.ViewConverter;
import com.thirty.user.enums.model.ViewType;
import com.thirty.user.mapper.ViewMapper;
import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;
import com.thirty.user.service.basic.ViewService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

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

    /**
     * 获取视图的所有祖先ID
     * @param viewId 视图ID
     * @return 祖先ID列表
     */
    @Override
    public List<Integer> getAncestorIds(Integer viewId) {
        List<Integer> ancestorIds = new ArrayList<>();
        List<View> views = list();

        Map<Integer, View> viewMap = View.buildViewMap(views);

        View currentView = viewMap.get(viewId);
        while (!Objects.equals(currentView.getParentNodeId(), RoleConstant.ROOT_ROLE_PARENT_ID)) {
            ancestorIds.add(currentView.getParentNodeId());
            currentView = viewMap.get(currentView.getParentNodeId());
        }

        return ancestorIds;
    }

    /**
     * 获取视图列表的所有祖先ID
     * @param viewIds 视图ID列表
     * @return 祖先ID列表
     */
    @Override
    public List<Integer> getAncestorIds(List<Integer> viewIds) {
        Set<Integer> ancestorIds = new HashSet<>();
        for (Integer viewId : viewIds) {
            ancestorIds.addAll(getAncestorIds(viewId));
        }
        return new ArrayList<>(ancestorIds);
    }

    /**
     * 获取视图的所有后代ID
     * @param viewId 视图ID
     * @return 后代ID列表
     */
    @Override
    public List<Integer> getDescendantIds(Integer viewId) {
        List<Integer> descendantIds = new ArrayList<>();
        List<View> views = list();

        Map<Integer, List<View>> parentChildMap = View.buildParentChildMap(views);

        List<View> currentLevel = parentChildMap.get(viewId);
        while (!CollectionUtils.isEmpty(currentLevel)) {
            List<View> nextLevel = new ArrayList<>();
            for (View view : currentLevel) {
                descendantIds.add(view.getId());
                nextLevel.addAll(parentChildMap.getOrDefault(view.getId(), new ArrayList<>()));
            }
            currentLevel = nextLevel;
        }

        return descendantIds;
    }

    /**
     * 获取视图列表的所有后代ID
     * @param viewIds 视图ID列表
     * @return 后代ID列表
     */
    @Override
    public List<Integer> getDescendantIds(List<Integer> viewIds)  {
        Set<Integer> descendantIds = new HashSet<>();
        for (Integer viewId : viewIds) {
            descendantIds.addAll(getDescendantIds(viewId));
        }
        return new ArrayList<>(descendantIds);
    }

}




