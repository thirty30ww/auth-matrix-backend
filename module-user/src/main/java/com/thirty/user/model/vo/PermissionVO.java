package com.thirty.user.model.vo;

import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.model.entity.Permission;
import lombok.Data;

import java.util.*;

@Data
public class PermissionVO {
    private Permission node;
    private boolean hasPermission = false;
    private boolean hasChange = false;
    private List<PermissionVO> children = new ArrayList<>();

    /**
     * 获取父节点id到子节点列表的映射
     * @param permissionVOS 节点列表
     * @return 父节点id到子节点列表的映射
     */
    public static Map<Integer, PermissionVO> buildParentChildMap(List<PermissionVO> permissionVOS) {
        Map<Integer, PermissionVO> parentChildMap = new HashMap<>();
        for (PermissionVO permissionVO : permissionVOS) {
            parentChildMap.put(permissionVO.getNode().getId(), permissionVO);
        }
        return parentChildMap;
    }

    /**
     * 获取相同父节点的视图VO列表
     * @param permissionVOS 节点列表
     * @param parentId 父节点id
     * @return 相同父节点的视图VO列表
     */
    public static List<PermissionVO> getSameParentViewVO(Collection<PermissionVO> permissionVOS, Integer parentId) {
        return permissionVOS.stream().filter(viewVO -> viewVO.getNode().getParentNodeId().equals(parentId)).toList();
    }

    /**
     * 按前端节点id排序
     * @param permissionVOS 节点列表
     * @return 按前端节点id排序后的节点列表
     */
    public static List<PermissionVO> sortByFrontNodeId(Collection<PermissionVO> permissionVOS) {
        List<PermissionVO> permissionVOList = new ArrayList<>(permissionVOS);
        permissionVOList.sort(Comparator.comparingInt(viewVO -> viewVO.getNode().getFrontNodeId()));
        return permissionVOList;
    }

    /**
     * 按前端节点id排序
     * @param permissionVOS 节点列表
     * @param parentId 父节点id
     * @return 按前端节点id排序后的节点列表
     */
    public static List<PermissionVO> sortByFrontNodeId(Collection<PermissionVO> permissionVOS, Integer parentId) {
        return sortByFrontNodeId(getSameParentViewVO(permissionVOS, parentId));
    }

    /**
     * 设置视图是否有权限
     * @param permittedViewIds 权限视图ID列表
     * @param menus 菜单列表
     */
    public static void setHasPermission(List<Integer> permittedViewIds, List<PermissionVO> menus) {
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
    public static void setHasChange(List<Integer> permittedViewIds, List<PermissionVO> menus) {
        menus.forEach(view -> view.setHasChange(permittedViewIds.contains(view.getNode().getId())));
    }

    /**
     * 过滤出菜单树中有权限的视图
     * @param permittedViewIds 权限视图ID列表
     * @param menus 菜单列表
     */
    public static void filterHasPermission(List<Integer> permittedViewIds, List<PermissionVO> menus) {
        menus.removeIf(view -> view.getNode().getType() != PermissionType.PAGE && !permittedViewIds.contains(view.getNode().getId()));
    }
}
