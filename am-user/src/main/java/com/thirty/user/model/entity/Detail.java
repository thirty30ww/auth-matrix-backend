package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.thirty.common.model.entity.BaseEntity;
import com.thirty.user.enums.model.UserSex;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户的具体信息
 * @TableName detail
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="detail")
@Data
public class Detail extends BaseEntity {
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
}