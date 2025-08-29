package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色表
 * @TableName role
 */
@TableName(value ="role")
@Data
public class Role implements Serializable {
    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 父角色的ID(如果没有则为0)
     */
    private Integer parentNodeId;

    /**
     * 层级(如果是根节点则为0)
     */
    private Integer level;

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

    /**
     * 从角色列表中提取角色ID
     * @param roles 角色列表
     * @return 角色ID列表
     */
    public static List<Integer> extractIds(Collection<Role> roles) {
        return roles.stream()
                .map(Role::getId)
                .collect(Collectors.toList());
    }

    /**
     * 从角色列表中提取全局角色
     * @param roles 角色列表
     * @return 全局角色列表
     */
    public static List<Role> getGlobalRoles(List<Role> roles) {
        return roles.stream()
                .filter(role -> role.getParentNodeId() == -1)
                .collect(Collectors.toList());
    }

    /**
     * 构建父角色ID到子角色列表的映射
     * @param roles 角色列表
     * @return 父角色ID到子角色列表的映射
     */
    public static Map<Integer, List<Role>> buildParentChildMap(List<Role> roles) {
        return roles.stream()
                .filter(role -> role.getParentNodeId() != 0)
                .collect(Collectors.groupingBy(Role::getParentNodeId));
    }
}