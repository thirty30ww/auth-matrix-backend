package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户和角色表
 * @TableName user_role
 */
@TableName(value ="user_role")
@Data
public class UserRole implements Serializable {
    /**
     * 用户角色ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户的ID(关联user表)
     */
    private Integer userId;

    /**
     * 角色的ID(关联role表)
     */
    private Integer roleId;

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
     * 从用户角色列表中提取角色ID列表
     * @param userRoles 用户角色列表
     * @return 角色ID列表
     */
    public static List<Integer> extractRoleIds(List<UserRole> userRoles) {
        return userRoles.stream().map(UserRole::getRoleId).distinct().collect(Collectors.toList());
    }
}