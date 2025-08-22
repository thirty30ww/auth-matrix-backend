package com.thirty.user.service;

import com.thirty.user.model.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.thirty.user.model.vo.RoleVO;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【role(角色表)】的数据库操作Service
* @createDate 2025-08-09 15:40:41
*/
public interface RoleService extends IService<Role> {
    /**
     * 获取角色树
     * @param username 用户名
     * @return 角色树
     */
    List<RoleVO> getRoleTree(String username);

    /**
     * 获取角色列表
     * @param username 用户名
     * @param isChild 是否仅获取子角色列表
     * @return 角色列表
     */
    List<Role> getRoleList(String username, Boolean isChild);

    /**
     * 获取全局角色列表
     * @return 全局角色列表
     */
    List<Role> getGlobalRoleList();
}
