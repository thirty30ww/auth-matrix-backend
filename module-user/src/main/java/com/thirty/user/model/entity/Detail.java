package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.thirty.user.enums.model.UserSex;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户的具体信息
 * @TableName detail
 */
@TableName(value ="detail")
@Data
public class Detail implements Serializable {
    /**
     * 用户ID
     */
    @TableId
    private Integer id;

    /**
     * 用户的名字
     */
    private String name;

    /**
     * 用户头像链接
     */
    private String avatarUrl;

    /**
     * 性别(1:男,2:女,3:未知)
     */
    private UserSex sex;

    /**
     * 签名
     */
    private String signature;

    /**
     * 创建时间
     * LocalDateTime 是不可变的，线程安全
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否被删除(1:是 0:否)
     */
    @TableLogic
    private Boolean isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}