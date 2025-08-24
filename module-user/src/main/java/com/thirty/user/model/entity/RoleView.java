package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}