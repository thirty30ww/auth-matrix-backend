package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.thirty.user.enums.model.ViewType;
import lombok.Data;

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}