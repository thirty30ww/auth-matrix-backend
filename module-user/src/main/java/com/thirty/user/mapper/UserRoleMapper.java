package com.thirty.user.mapper;

import com.thirty.user.model.entity.Role;
import com.thirty.user.model.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【user_role(用户和角色表)】的数据库操作Mapper
* @createDate 2025-08-09 15:40:20
* @Entity com.thirty.user.model/entity.UserRole
*/
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户ID查询角色名称列表
     * @param userId 用户ID
     * @return 角色名称列表
     */
    List<Role> selectRolesByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户ID列表查询角色ID列表
     * @param userIds 用户ID列表
     * @return 角色ID列表
     */
    List<Integer> selectRoleIdsByUserIds(@Param("userIds") List<Integer> userIds);

    /**
     * 为用户添加角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    void addUserRoles(@Param("userId") Integer userId, @Param("roleIds") List<Integer> roleIds);
}




