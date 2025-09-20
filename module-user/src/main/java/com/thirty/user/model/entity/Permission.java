package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.thirty.user.enums.model.PermissionType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 页面表
 * @TableName view
 */
@TableName(value ="permission")
@Data
public class Permission implements Serializable {
    /**
     * 菜单ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

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
    private Integer parentNodeId;

    /**
     * 该节点的前一个节点ID(若为第1个节点则取0)
     */
    private Integer frontNodeId;

    /**
     * 该节点的后继节点ID
     */
    private Integer behindNodeId;

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否启用(1:有效 0:无效)
     */
    private Boolean isValid;

    /**
     * 是否被删除(1:是 0:否)
     */
    @TableLogic
    private Boolean isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 构建无效视图列表
     * @param permissionIds 权限ID列表
     * @return 无效权限列表
     */
    public static List<Permission> toNotValidPermission(List<Integer> permissionIds) {
        return permissionIds.stream().map(permissionId -> {
            Permission permission = new Permission();
            permission.setId(permissionId);
            permission.setIsValid(false);
            return permission;
        }).collect(Collectors.toList());
    }

    /**
     * 构建视图Map，key为视图ID，value为View
     * @param permissions 视图列表
     * @return 视图Map
     */
    public static Map<Integer, Permission> buildMap(List<Permission> permissions) {
        return permissions.stream().collect(Collectors.toMap(Permission::getId, view -> view));
    }

    /**
     * 构建父节点ID和子节点列表的Map，key为父节点ID，value为子节点列表
     * @param permissions 视图列表
     * @return 父节点ID和子节点列表的Map
     */
    public static Map<Integer, List<Permission>> buildParentChildMap(List<Permission> permissions) {
        return permissions.stream().collect(Collectors.groupingBy(Permission::getParentNodeId));
    }

    /**
     * 从视图列表中提取视图ID列表
     * @param permissions 视图列表
     * @return 视图ID列表
     */
    public static List<Integer> extractViewIds(List<Permission> permissions) {
        return permissions.stream().map(Permission::getId).distinct().collect(Collectors.toList());
    }

    /**
     * 从视图列表中提取权限码列表
     * @param permissions 视图列表
     * @return 权限码列表
     */
    public static List<String> extractPermissionCodes(List<Permission> permissions) {
        return permissions.stream().map(Permission::getPermissionCode).distinct().collect(Collectors.toList());
    }

    /**
     * 从视图列表中提取最大的前一个节点ID的视图
     * @param permissions 视图列表
     * @return 最大的前一个节点ID的视图
     */
    public static Permission extractMaxFrontIdView(List<Permission> permissions) {
        return permissions.stream().max(Comparator.comparingInt(Permission::getFrontNodeId)).orElse(null);
    }
}