package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.thirty.common.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户偏好设置
 * @TableName preference
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="preference")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Preference extends BaseEntity {
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
}