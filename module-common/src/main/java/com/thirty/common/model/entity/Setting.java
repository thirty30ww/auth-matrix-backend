package com.thirty.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}