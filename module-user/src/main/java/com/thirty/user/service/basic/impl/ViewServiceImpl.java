package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.constant.RoleConstant;
import com.thirty.user.constant.ViewConstant;
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
     * 获取目录VO列表
     * @return 目录VO列表
     */
    @Override
    public List<ViewVO> getDirectoryViewVOS() {
        List<View> directoryViews = getDirectoryViews();
        return ViewConverter.INSTANCE.toViewVOS(directoryViews);
    }

    /**
     * 获取目录列表
     * @return 目录列表
     */
    @Override
    public List<View> getDirectoryViews() {
        QueryWrapper<View> wrapper = new QueryWrapper<>();
        wrapper.eq("type", ViewType.DIRECTORY);
        return list(wrapper);
    }

    /**
     * 获取菜单和按钮列表
     * @return 菜单列表
     */
    @Override
    public List<View> getMenuAndButtonViews() {
        QueryWrapper<View> wrapper = new QueryWrapper<>();
        wrapper.eq("type", ViewType.DIRECTORY).or()
                .eq("type", ViewType.MENU).or()
                .eq("type", ViewType.BUTTON);
        return list(wrapper);
    }

    /**
     * 获取菜单和按钮VO列表
     * @return 菜单和按钮VO列表
     */
    @Override
    public List<ViewVO> getMenuAndButtonViewVOS() {
        List<View> menuAndButtonViews = getMenuAndButtonViews();
        return ViewConverter.INSTANCE.toViewVOS(menuAndButtonViews);
    }

    /**
     * 获取菜单列表
     * @return 菜单列表
     */
    @Override
    public List<View> getMenuViews() {
        QueryWrapper<View> menuWrapper = new QueryWrapper<>();
        menuWrapper.eq("type", ViewType.DIRECTORY).or()
                .eq("type", ViewType.MENU);
        return list(menuWrapper);
    }

    /**
     * 获取非页面视图VO列表
     * @return 非页面视图VO列表
     */
    @Override
    public List<ViewVO> getMenuViewVOS() {
        List<View> notPageViews = getMenuViews();
        return ViewConverter.INSTANCE.toViewVOS(notPageViews);
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
        List<ViewVO> responses = ViewConverter.INSTANCE.toViewVOS(pageViews);
        responses.forEach(viewVO -> viewVO.setHasPermission(true));
        return responses;
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

        Map<Integer, View> viewMap = View.buildMap(views);

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

    /**
     * 获取视图列表的所有权限码
     * @param viewIds 视图ID列表
     * @return 权限码列表
     */
    @Override
    public List<String> getPermissionCodes(List<Integer> viewIds) {
        QueryWrapper<View> wrapper = new QueryWrapper<>();
        wrapper.in("id", viewIds)
                .eq("is_valid", true)
                .eq("type", ViewType.BUTTON);
        List<View> views = list(wrapper);
        return View.extractPermissionCodes(views);
    }

    /**
     * 修改视图
     * @param view 视图
     */
    @Override
    public void modifyView(View view) {
        View oldView = getById(view.getId());
        if (!Objects.equals(view.getParentNodeId(), oldView.getParentNodeId())) {
            tailInsert(view);
            connectNeighborViews(oldView);
            return;
        }

        // 如果视图状态从有效变为无效，需要更新所有后代视图
        if (!view.getIsValid() && oldView.getIsValid()) {
            List<Integer> descendantIds = getDescendantIds(view.getId());
            List<View> descendantViews = View.toNotValidView(descendantIds);
            updateBatchById(descendantViews);
        }
        updateById(view);
    }

    /**
     * 尾插法添加视图
     * @param view 视图
     */
    @Override
    public void tailInsert(View view) {
        View tailView = getTailNode(view.getParentNodeId());
        if (tailView == null) {
            view.setFrontNodeId(ViewConstant.HEAD_VIEW_FRONT_ID);
        } else {
            view.setFrontNodeId(tailView.getId());
        }

        if (view.getId() == null) {
            save(view);
        } else {
            updateById(view);
        }

        // 处理新视图前一个视图的的behindNodeId
        if (tailView != null) {
            tailView.setBehindNodeId(view.getId());
            updateById(tailView);
        }
    }

    /**
     * 连接视图的邻居节点
     * @param view 视图
     */
    @Override
    public void connectNeighborViews(View view) {
        List<View> neighborViews = new ArrayList<>();
        Map<Integer, View> viewMap = View.buildMap(getByParentId(view.getParentNodeId()));
        if (!Objects.equals(view.getFrontNodeId(), ViewConstant.HEAD_VIEW_FRONT_ID)) {
            View frontView = viewMap.get(view.getFrontNodeId());
            frontView.setBehindNodeId(view.getBehindNodeId());
            neighborViews.add(frontView);
        }
        if (!Objects.equals(view.getBehindNodeId(), ViewConstant.TAIL_VIEW_BEHIND_ID)) {
            View behindView = viewMap.get(view.getBehindNodeId());
            behindView.setFrontNodeId(view.getFrontNodeId());
            neighborViews.add(behindView);
        }
        updateBatchById(neighborViews);
    }

    /**
     * 删除视图
     * @param viewId 视图ID
     */
    @Override
    public void deleteView(Integer viewId) {
        View view = getById(viewId);

        List<Integer> descendantIds = getDescendantIds(viewId);
        descendantIds.add(viewId);
        removeByIds(descendantIds);

        connectNeighborViews(view);
    }

    /**
     * 视图上移
     * @param viewId 视图ID
     */
    @Override
    public void moveUp(Integer viewId) {
        View view = getById(viewId);
        View frontView = getById(view.getFrontNodeId());

        if (frontView == null) {
            return;
        }

        frontView.setBehindNodeId(view.getBehindNodeId());

        view.setFrontNodeId(frontView.getFrontNodeId());
        view.setBehindNodeId(frontView.getId());

        frontView.setFrontNodeId(viewId);

        List<View> modifyViews = List.of(view, frontView);
        updateBatchById(modifyViews);
    }

    /**
     * 视图下移
     * @param viewId 视图ID
     */
    @Override
    public void moveDown(Integer viewId) {
        View view = getById(viewId);
        View behindView = getById(view.getBehindNodeId());

        if (behindView == null) {
            return;
        }

        behindView.setFrontNodeId(view.getFrontNodeId());

        view.setBehindNodeId(behindView.getBehindNodeId());
        view.setFrontNodeId(behindView.getId());

        behindView.setBehindNodeId(viewId);

        List<View> modifyViews = List.of(view, behindView);
        updateBatchById(modifyViews);
    }

    /**
     * 获取视图的尾节点
     * @param parentId 父节点ID
     * @return 尾节点
     */
    @Override
    public View getTailNode(Integer parentId) {
        List<View> views = getByParentId(parentId);
        return View.extractMaxFrontIdView(views);
    }

    /**
     * 根据父节点ID获取视图列表
     * @param parentId 父节点ID
     * @return 视图列表
     */
    @Override
    public List<View> getByParentId(Integer parentId) {
        QueryWrapper<View> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_node_id", parentId);
        return list(wrapper);
    }
}




