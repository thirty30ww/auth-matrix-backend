package com.thirty.user.service.domain.view.impl;

import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;
import com.thirty.user.service.basic.RoleService;
import com.thirty.user.service.basic.RoleViewService;
import com.thirty.user.service.basic.ViewService;
import com.thirty.user.service.domain.view.ViewQueryDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ViewQueryDomainImpl implements ViewQueryDomain {

    @Resource
    private ViewService viewService;
    @Resource
    private RoleViewService roleViewService;
    @Resource
    private RoleService roleService;

    /**
     * 获取菜单树
     * @param currentRoleIds 当前角色id列表
     * @param targetRoleId 目标角色id列表
     * @return 菜单树
     */
    @Override
    public List<ViewVO> getMenuTree(List<Integer> currentRoleIds, Integer targetRoleId) {
        // 所有菜单的map
        Map<Integer, ViewVO> menuMap = viewService.getNotPageViewVOMap();

        // 当前角色的菜单id列表
        List<Integer> currentViewIds = getMenuIds(currentRoleIds);

        // 过滤出当前角色有权限查看的菜单
        filterViewHasPermission(currentViewIds, menuMap);

        // 目标角色的菜单id列表
        List<Integer> targetViewIds = getMenuIds(targetRoleId);

        // 根据目标角色有权限查看的菜单设置menuMap中所有viewVO的hasPermission属性
        setViewHasPermission(targetViewIds, menuMap);

        // 构建树
        return getTreeByParentNodeId(menuMap, 0);
    }

    /**
     * 获取菜单树
     * @param currentRoleIds 当前角色id列表
     * @return 菜单树
     */
    @Override
    public List<ViewVO> getMenuTree(List<Integer> currentRoleIds) {
        // 所有菜单的map
        Map<Integer, ViewVO> menuMap = viewService.getNotPageViewVOMap();

        // 当前角色有权限查看的菜单id列表
        List<Integer> permittedViewIds = getMenuIds(currentRoleIds);

        // 过滤出当前角色有权限查看的菜单
        filterViewHasPermission(permittedViewIds, menuMap);

        // 构建树
        return getTreeByParentNodeId(menuMap, 0);
    }

    /**
     * 获取菜单树
     * @return 菜单树
     */
    @Override
    public List<ViewVO> getMenuTree() {
        Map<Integer, ViewVO> menuMap = viewService.getNotPageViewVOMap();
        return getTreeByParentNodeId(menuMap, 0);
    }

    /**
     * 获取视图树
     * @return 视图树
     */
    @Override
    public List<ViewVO> getViewTree() {
        // 菜单节点
        List<ViewVO> menuTree = getMenuTree();
        // 页面节点
        List<ViewVO> pages = viewService.getPageViewVOS();

        // 视图树
        List<ViewVO> viewTree = new ArrayList<>();
        viewTree.addAll(menuTree);
        viewTree.addAll(pages);

        return viewTree;
    }


    /**
     * 查找第一个节点
     * @param viewVoMap 节点map
     * @param parentNodeId 父节点id
     * @return 第一个节点
     */
    private ViewVO findFirstViewVO(Map<Integer, ViewVO> viewVoMap, Integer parentNodeId) {
        for (ViewVO viewVO : viewVoMap.values()) {
            View node = viewVO.getNode();
            if (node.getParentNodeId().equals(parentNodeId) && node.getFrontNodeId().equals(0)) {
                return viewVO;
            }
        }
        return null;
    }

    /**
     * 根据父节点id构建树
     * @param viewVOMap 节点map
     * @param parentNodeId 父节点id
     * @return 树
     */
    private List<ViewVO> getTreeByParentNodeId(Map<Integer, ViewVO> viewVOMap, Integer parentNodeId) {
        // 按照节点顺序构建树
        ViewVO currentViewVO = findFirstViewVO(viewVOMap, parentNodeId);
        List<ViewVO> response = new ArrayList<>();

        // 按照节点顺序构建树
        while (currentViewVO != null) {
            View node = currentViewVO.getNode();

            currentViewVO.setChildren(getTreeByParentNodeId(viewVOMap, node.getId()));
            response.add(currentViewVO);

            Integer nextNodeId = node.getBehindNodeId();
            currentViewVO = nextNodeId != 0 ? viewVOMap.get(nextNodeId) : null;
        }

        return response;
    }

    /**
     * 获取菜单id列表
     * @param roleId 角色id
     * @return 菜单id列表
     */
    private List<Integer> getMenuIds(Integer roleId) {
        Integer level = roleService.getById(roleId).getLevel();
        if (level == 0) {
            // 最高级角色，返回所有菜单
            return viewService.getNotPageViews().stream().map(View::getId).toList();
        }
        // 否则返回角色菜单列表
        return roleViewService.getViewIds(roleId);
    }

    /**
     * 获取菜单id列表
     * @param roleIds 角色id列表
     * @return 菜单id列表
     */
    private List<Integer> getMenuIds(List<Integer> roleIds) {
        // 获取最高级角色
        Integer highestLevel = roleService.getHighestLevel(roleIds);
        if (highestLevel ==  0) {
            // 最高级角色为0，说明是最高级角色，返回所有菜单
            return viewService.getNotPageViews().stream().map(View::getId).toList();
        }
        // 否则返回角色菜单列表
        return roleViewService.getViewIds(roleIds);
    }

    /**
     * 设置视图是否有权限
     * @param permittedViewIds 权限视图ID列表
     * @param menuMap 菜单map
     */
    private void setViewHasPermission(List<Integer> permittedViewIds, Map<Integer, ViewVO> menuMap) {
        menuMap.values().forEach(viewVO -> {
            viewVO.setHasPermission(permittedViewIds.contains(viewVO.getNode().getId()));
        });
    }

    /**
     * 过滤出菜单树中有权限的视图
     * @param permittedViewIds 权限视图ID列表
     * @param menuMap 菜单map
     */
    private void filterViewHasPermission(List<Integer> permittedViewIds, Map<Integer, ViewVO> menuMap) {
        menuMap.values().removeIf(view -> !permittedViewIds.contains(view.getNode().getId()));
    }
}
