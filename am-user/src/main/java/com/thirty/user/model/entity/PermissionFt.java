package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.thirty.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 前台权限表
 * @TableName permission_ft
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="permission_ft")
@Data
public class PermissionFt extends BaseEntity {
    /**
     * 权限名
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
     * 权限码
     */
    private String permission_code;

    /**
     * 类型 1-页面 2-按钮
     */
    private Integer type;

    /**
     * 父权限ID
     */
    private Integer parent_id;

    /**
     * 前一个权限的ID
     */
    private Integer front_id;

    /**
     * 后一个权限ID
     */
    private Integer behind_id;

    /**
     * 是否需要验证登录
     */
    private Integer need_verify;

    /**
     * 是否有效
     */
    private Integer is_valid;
}