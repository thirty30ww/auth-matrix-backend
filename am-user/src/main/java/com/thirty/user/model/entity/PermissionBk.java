package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.thirty.common.model.entity.BaseEntity;
import com.thirty.user.enums.model.PermissionType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 页面表
 * @TableName view
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="permission_bk")
@Data
public class PermissionBk extends BaseEntity {
    /**
     * 菜单名
     */
    private String name;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 父节点ID(无父结点则为0)
     */
    private Integer parentId;

    /**
     * 该节点的前一个节点ID(若为第1个节点则取0)
     */
    private Integer frontId;

    /**
     * 该节点的后继节点ID
     */
    private Integer behindId;

    /**
     * 菜单的图标
     */
    private String icon;

    /**
     * 页面类型(1:目录, 2:菜单, 3:页面)
     */
    private PermissionType type;

    /**
     * 权限码
     */
    private String permissionCode;

    /**
     * 是否启用(1:有效 0:无效)
     */
    private Boolean isValid;

    /**
     * 构建无效权限列表
     * @param permissionIds 权限ID列表
     * @return 无效权限列表
     */
    public static List<PermissionBk> toNotValidPermission(List<Integer> permissionIds) {
        return permissionIds.stream().map(permissionId -> {
            PermissionBk permissionBk = new PermissionBk();
            permissionBk.setId(permissionId);
            permissionBk.setIsValid(false);
            return permissionBk;
        }).collect(Collectors.toList());
    }

    /**
     * 构建权限Map，key为权限ID，value为View
     * @param permissionBks 权限列表
     * @return 权限Map
     */
    public static Map<Integer, PermissionBk> buildMap(List<PermissionBk> permissionBks) {
        return permissionBks.stream().collect(Collectors.toMap(PermissionBk::getId, view -> view));
    }

    /**
     * 构建父节点ID和子节点列表的Map，key为父节点ID，value为子节点列表
     * @param permissionBks 权限列表
     * @return 父节点ID和子节点列表的Map
     */
    public static Map<Integer, List<PermissionBk>> buildParentChildMap(List<PermissionBk> permissionBks) {
        return permissionBks.stream().collect(Collectors.groupingBy(PermissionBk::getParentId));
    }

    /**
     * 从权限列表中提取权限ID列表
     * @param permissionBks 权限列表
     * @return 权限ID列表
     */
    public static List<Integer> extractViewIds(List<PermissionBk> permissionBks) {
        return permissionBks.stream().map(PermissionBk::getId).distinct().collect(Collectors.toList());
    }

    /**
     * 从权限列表中提取权限码列表
     * @param permissionBks 权限列表
     * @return 权限码列表
     */
    public static List<String> extractPermissionCodes(List<PermissionBk> permissionBks) {
        return permissionBks.stream().map(PermissionBk::getPermissionCode).distinct().collect(Collectors.toList());
    }

    /**
     * 从权限列表中提取最大的前一个节点ID的权限
     * @param permissionBks 权限列表
     * @return 最大的前一个节点ID的权限
     */
    public static PermissionBk extractMaxFrontIdPermission(List<PermissionBk> permissionBks) {
        return permissionBks.stream().max(Comparator.comparingInt(PermissionBk::getFrontId)).orElse(null);
    }
}