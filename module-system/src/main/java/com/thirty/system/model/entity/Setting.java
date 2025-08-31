package com.thirty.system.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 全局设置表
 * @TableName setting
 */
@TableName(value ="setting")
@Data
public class Setting implements Serializable {
    /**
     * 设置ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 设置的字段名
     */
    private String field;

    /**
     * 设置的描述
     */
    private String description;

    /**
     * 设置的值
     */
    private Integer value;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否被删除(1:是 0:否)
     */
    @TableLogic
    private Boolean isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}