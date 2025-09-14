package com.thirty.user.model.vo;

import com.thirty.user.enums.model.ViewType;
import com.thirty.user.model.entity.View;
import lombok.Data;

import java.util.*;

@Data
public class ViewVO {
    private View node;
    private boolean hasPermission = false;
    private boolean hasChange = false;
    private List<ViewVO> children = new ArrayList<>();

    /**
     * 获取父节点id到子节点列表的映射
     * @param viewVOS 节点列表
     * @return 父节点id到子节点列表的映射
     */
    public static Map<Integer, ViewVO> buildParentChildMap(List<ViewVO> viewVOS) {
        Map<Integer, ViewVO> parentChildMap = new HashMap<>();
        for (ViewVO viewVO : viewVOS) {
            parentChildMap.put(viewVO.getNode().getId(), viewVO);
        }
        return parentChildMap;
    }

    /**
     * 获取相同父节点的视图VO列表
     * @param viewVOS 节点列表
     * @param parentId 父节点id
     * @return 相同父节点的视图VO列表
     */
    public static List<ViewVO> getSameParentViewVO(Collection<ViewVO> viewVOS, Integer parentId) {
        return viewVOS.stream().filter(viewVO -> viewVO.getNode().getParentNodeId().equals(parentId)).toList();
    }

    /**
     * 按前端节点id排序
     * @param viewVOS 节点列表
     * @return 按前端节点id排序后的节点列表
     */
    public static List<ViewVO> sortByFrontNodeId(Collection<ViewVO> viewVOS) {
        List<ViewVO> viewVOList = new ArrayList<>(viewVOS);
        viewVOList.sort(Comparator.comparingInt(viewVO -> viewVO.getNode().getFrontNodeId()));
        return viewVOList;
    }

    /**
     * 按前端节点id排序
     * @param viewVOs 节点列表
     * @param parentId 父节点id
     * @return 按前端节点id排序后的节点列表
     */
    public static List<ViewVO> sortByFrontNodeId(Collection<ViewVO> viewVOs, Integer parentId) {
        return sortByFrontNodeId(getSameParentViewVO(viewVOs, parentId));
    }

    /**
     * 设置视图是否有权限
     * @param permittedViewIds 权限视图ID列表
     * @param menus 菜单列表
     */
    public static void setHasPermission(List<Integer> permittedViewIds, List<ViewVO> menus) {
        menus.forEach(viewVO -> {
            if (permittedViewIds.contains(viewVO.getNode().getId())) {
                viewVO.setHasPermission(true);
            }
        });
    }

    /**
     * 设置视图是否有变更
     * @param permittedViewIds 权限视图ID列表
     * @param menus 菜单列表
     */
    public static void setHasChange(List<Integer> permittedViewIds, List<ViewVO> menus) {
        menus.forEach(view -> view.setHasChange(permittedViewIds.contains(view.getNode().getId())));
    }

    /**
     * 过滤出菜单树中有权限的视图
     * @param permittedViewIds 权限视图ID列表
     * @param menus 菜单列表
     */
    public static void filterHasPermission(List<Integer> permittedViewIds, List<ViewVO> menus) {
        menus.removeIf(view -> view.getNode().getType() != ViewType.PAGE && !permittedViewIds.contains(view.getNode().getId()));
    }
}
