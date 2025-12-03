package com.thirty.user.model.vo;

import com.thirty.user.enums.model.PermissionBkType;
import com.thirty.user.model.entity.PermissionBk;
import lombok.Data;

import java.util.*;

@Data
public class PermissionBkVO {
    private PermissionBk node;
    private boolean hasPermission = false;
    private boolean hasChange = false;
    private List<PermissionBkVO> children = new ArrayList<>();

    /**
     * 获取父节点id到子节点列表的映射
     * @param permissionBkVOS 节点列表
     * @return 父节点id到子节点列表的映射
     */
    public static Map<Integer, PermissionBkVO> buildParentChildMap(List<PermissionBkVO> permissionBkVOS) {
        Map<Integer, PermissionBkVO> parentChildMap = new HashMap<>();
        for (PermissionBkVO permissionBkVO : permissionBkVOS) {
            parentChildMap.put(permissionBkVO.getNode().getId(), permissionBkVO);
        }
        return parentChildMap;
    }

    /**
     * 获取相同父节点的权限VO列表
     * @param permissionBkVOS 节点列表
     * @param parentId 父节点id
     * @return 相同父节点的权限VO列表
     */
    public static List<PermissionBkVO> getSameParentViewVO(Collection<PermissionBkVO> permissionBkVOS, Integer parentId) {
        return permissionBkVOS.stream().filter(viewVO -> viewVO.getNode().getParentId().equals(parentId)).toList();
    }

    /**
     * 按前端节点id排序
     * @param permissionBkVOS 节点列表
     * @return 按前端节点id排序后的节点列表
     */
    public static List<PermissionBkVO> sortByFrontNodeId(Collection<PermissionBkVO> permissionBkVOS) {
        List<PermissionBkVO> permissionBkVOList = new ArrayList<>(permissionBkVOS);
        permissionBkVOList.sort(Comparator.comparingInt(viewVO -> viewVO.getNode().getFrontId()));
        return permissionBkVOList;
    }

    /**
     * 按前端节点id排序
     * @param permissionBkVOS 节点列表
     * @param parentId 父节点id
     * @return 按前端节点id排序后的节点列表
     */
    public static List<PermissionBkVO> sortByFrontNodeId(Collection<PermissionBkVO> permissionBkVOS, Integer parentId) {
        return sortByFrontNodeId(getSameParentViewVO(permissionBkVOS, parentId));
    }

    /**
     * 设置权限是否有权限
     * @param permittedViewIds 权限权限ID列表
     * @param menus 菜单列表
     */
    public static void setHasPermission(List<Integer> permittedViewIds, List<PermissionBkVO> menus) {
        menus.forEach(viewVO -> {
            if (permittedViewIds.contains(viewVO.getNode().getId())) {
                viewVO.setHasPermission(true);
            }
        });
    }

    /**
     * 设置权限是否有变更
     * @param permittedViewIds 权限权限ID列表
     * @param menus 菜单列表
     */
    public static void setHasChange(List<Integer> permittedViewIds, List<PermissionBkVO> menus) {
        menus.forEach(view -> view.setHasChange(permittedViewIds.contains(view.getNode().getId())));
    }

    /**
     * 过滤出菜单树中有权限的权限
     * @param permittedViewIds 权限权限ID列表
     * @param menus 菜单列表
     */
    public static void filterHasPermission(List<Integer> permittedViewIds, List<PermissionBkVO> menus) {
        menus.removeIf(view -> view.getNode().getType() != PermissionBkType.PAGE && !permittedViewIds.contains(view.getNode().getId()));
    }
}
