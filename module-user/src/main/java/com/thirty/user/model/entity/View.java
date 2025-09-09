package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.thirty.user.enums.model.ViewType;
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
@TableName(value ="view")
@Data
public class View implements Serializable {
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
    private ViewType type;

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
     * 构建视图Map，key为视图ID，value为View
     * @param views 视图列表
     * @return 视图Map
     */
    public static Map<Integer, View> buildMap(List<View> views) {
        return views.stream().collect(Collectors.toMap(View::getId, view -> view));
    }

    /**
     * 构建父节点ID和子节点列表的Map，key为父节点ID，value为子节点列表
     * @param views 视图列表
     * @return 父节点ID和子节点列表的Map
     */
    public static Map<Integer, List<View>> buildParentChildMap(List<View> views) {
        return views.stream().collect(Collectors.groupingBy(View::getParentNodeId));
    }

    /**
     * 从视图列表中提取视图ID列表
     * @param views 视图列表
     * @return 视图ID列表
     */
    public static List<Integer> extractViewIds(List<View> views) {
        return views.stream().map(View::getId).distinct().collect(Collectors.toList());
    }

    /**
     * 从视图列表中提取权限码列表
     * @param views 视图列表
     * @return 权限码列表
     */
    public static List<String> extractPermissionCodes(List<View> views) {
        return views.stream().map(View::getPermissionCode).distinct().collect(Collectors.toList());
    }

    /**
     * 从视图列表中提取最大的前一个节点ID的视图
     * @param views 视图列表
     * @return 最大的前一个节点ID的视图
     */
    public static View extractMaxFrontIdView(List<View> views) {
        return views.stream().max(Comparator.comparingInt(View::getFrontNodeId)).orElse(null);
    }
}