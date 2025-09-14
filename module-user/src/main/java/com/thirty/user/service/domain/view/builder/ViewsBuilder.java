package com.thirty.user.service.domain.view.builder;

import com.thirty.user.constant.RoleConstant;
import com.thirty.user.enums.model.ViewsType;
import com.thirty.user.model.vo.ViewVO;
import com.thirty.user.service.basic.ViewService;
import com.thirty.user.service.domain.view.ViewQueryDomain;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE) // 所有字段默认私有
public class ViewsBuilder {

    final ViewService viewService;
    final ViewQueryDomain viewQueryDomain;

    public ViewsBuilder(ViewService viewService, ViewQueryDomain viewQueryDomain) {
        this.viewService = viewService;
        this.viewQueryDomain = viewQueryDomain;
    }

    List<Integer> currentRoleIds;
    Integer targetRoleId;
    String keyword;

    ViewsType viewsType;
    boolean filterPermission = false;
    boolean setChangeFlag = false;
    boolean setPermissionFlag = false;

    /**
     * 设置搜索关键词
     */
    public ViewsBuilder withKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    /**
     * 设置当前角色ID列表
     */
    public ViewsBuilder forRoles(List<Integer> currentRoleIds) {
        this.currentRoleIds = currentRoleIds;
        return this;
    }

    /**
     * 设置目标角色ID
     */
    public ViewsBuilder withTargetRole(Integer targetRoleId) {
        this.targetRoleId = targetRoleId;
        return this;
    }

    /**
     * 设置视图类型
     */
    public ViewsBuilder ofType(ViewsType viewsType) {
        this.viewsType = viewsType;
        return this;
    }

    /**
     * 过滤权限（只返回有权限的视图）
     */
    public ViewsBuilder filterByPermission() {
        this.filterPermission = true;
        return this;
    }

    /**
     * 设置变更标志（为每个视图设置hasChange属性）
     */
    public ViewsBuilder withChangeFlag() {
        this.setChangeFlag = true;
        return this;
    }

    /**
     * 设置权限标志（为每个视图设置hasPermission属性）
     */
    public ViewsBuilder withPermissionFlag() {
        this.setPermissionFlag = true;
        return this;
    }

    /**
     * 构建成列表（平铺结构）
     */
    public List<ViewVO> build() {
        List<ViewVO> views = getViewsByType();
        return processPermissions(views);
    }

    /**
     * 构建成树结构
     */
    public List<ViewVO> buildTree() {
        List<ViewVO> views = build();
        return getTreeByParentNodeId(views, RoleConstant.ROOT_ROLE_PARENT_ID);
    }

    /**
     * 根据类型获取视图列表
     */
    private List<ViewVO> getViewsByType() {
        if (viewsType == null) {
            viewsType = ViewsType.MENU; // 默认为菜单
        }

        return switch (viewsType) {
            case MENU -> viewService.getMenuViewVOS();
            case MENU_AND_BUTTON -> viewService.getMenuAndButtonViewVOS();
            case PAGE -> viewService.getPageViewVOS();
            case VIEW -> {
                // 视图：菜单 + 页面
                List<ViewVO> views = new ArrayList<>();
                views.addAll(viewService.getMenuViewVOS());
                views.addAll(viewService.getPageViewVOS());
                yield views;
            }
            case DIRECTORY -> viewService.getDirectoryViewVOS();
            case NOT_DIRECTORY_AND_BUTTON -> viewService.getNotDirectoryAndButtonViewVOS(keyword);
        };
    }

    /**
     * 处理权限相关逻辑
     */
    private List<ViewVO> processPermissions(List<ViewVO> views) {
        if (CollectionUtils.isEmpty(currentRoleIds)) {
            return views;
        }

        // 获取当前角色的权限视图ID
        List<Integer> currentViewIds = viewQueryDomain.getPermissionId(currentRoleIds);

        // 过滤权限
        if (filterPermission) {
            ViewVO.filterHasPermission(currentViewIds, views);
        } else if (setChangeFlag) {
            ViewVO.setHasChange(currentViewIds, views);
        }

        // 设置权限标志
        if (setPermissionFlag) {
            if (targetRoleId != null) {
                // 目标角色的权限视图ID
                List<Integer> targetViewIds = viewQueryDomain.getPermissionId(targetRoleId);
                ViewVO.setHasPermission(targetViewIds, views);
            } else {
                ViewVO.setHasPermission(currentViewIds, views);
            }
        }

        return views;
    }

    /**
     * 根据父节点ID构建树
     */
    private List<ViewVO> getTreeByParentNodeId(List<ViewVO> views, Integer parentNodeId) {
        // 获取同一个父节点的ViewVO列表并且按FrontNodeId的从小到大排序
        List<ViewVO> viewVOS = ViewVO.sortByFrontNodeId(views, parentNodeId);

        // 记录返回结果
        List<ViewVO> responses = new ArrayList<>();

        // 遍历排序后的ViewVO列表
        viewVOS.forEach(viewVO -> {
            // 递归构建子树
            viewVO.setChildren(getTreeByParentNodeId(views, viewVO.getNode().getId()));
            // 添加到返回结果
            responses.add(viewVO);
        });

        return responses;
    }
}
