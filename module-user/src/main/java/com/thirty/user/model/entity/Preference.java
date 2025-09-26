package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户偏好设置
 * @TableName preference
 */
@TableName(value ="preference")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Preference implements Serializable {
    /**
     * 用户偏好设置ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID（关联user表）
     */
    private Integer userId;

    /**
     * 偏好字段名
     */
    private String field;

    /**
     * 偏好值
     */
    private String value;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否被删除
     */
    @TableLogic
    private Integer isDelete;

    public Preference(Integer userId, String field, String value) {
        this.userId = userId;
        this.field = field;
        this.value = value;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}