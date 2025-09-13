package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色页面表
 * @TableName role_view
 */
@TableName(value ="role_view")
@Data
public class RoleView implements Serializable {
    /**
     * 角色页面ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 角色ID（关联role表）
     */
    private Integer roleId;

    /**
     * 页面ID（关联view表）
     */
    private Integer viewId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否被删除(1:是 0:否)
     */
    @TableLogic
    private Boolean isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 从角色视图列表中提取视图id列表
     * @param roleViews 角色视图列表
     * @return 视图id列表
     */
    public static List<Integer> extractViewIds(List<RoleView> roleViews) {
        return roleViews.stream().map(RoleView::getViewId).distinct().collect(Collectors.toList());
    }

    /**
     * 拼接角色视图id列表（返回格式roleId_viewId）
     * @param roleViews 角色视图列表
     * @return 拼接后的角色视图id列表
     */
    public static List<String> spliceRoleViewIds(List<RoleView> roleViews) {
        return roleViews.stream().map(rv -> rv.getRoleId() + "_" + rv.getViewId()).distinct().collect(Collectors.toList());
    }
}