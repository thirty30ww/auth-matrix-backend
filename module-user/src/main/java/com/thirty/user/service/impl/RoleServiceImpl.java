package com.thirty.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.common.api.SettingApi;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.entity.User;
import com.thirty.user.model.vo.RoleVO;
import com.thirty.user.service.RoleService;
import com.thirty.user.mapper.RoleMapper;
import com.thirty.user.service.UserRoleService;
import com.thirty.user.utils.RoleUtil;
import com.thirty.user.utils.UserUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author Lenovo
* @description 针对表【role(角色表)】的数据库操作Service实现
* @createDate 2025-08-09 15:40:41
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private UserUtil userUtil;

    @Resource
    private RoleUtil roleUtil;

    @Resource
    private SettingApi settingApi;

    /**
     * 获取角色树
     * @param username 用户名
     * @return 角色树
     */
    @Override
    public List<RoleVO> getRoleTree(String username) {
        // 获取所有角色
        List<Role> allRoles = list();
        // 获取当前用户有权限的角色
        List<Role> permittedRoles = getRoleList(username, true);

        Map<Integer, RoleVO> roleVOMap;
        // 是否仅显示有权限操作的角色
        if (settingApi.isPermissionDisplay()) {
            roleVOMap = roleUtil.buildRoleVOMap(permittedRoles);
        } else {
            roleVOMap = roleUtil.buildRoleVOMap(allRoles, permittedRoles);
        }

        // 构建树形结构并返回根节点
        return roleUtil.buildTree(allRoles, roleVOMap);
    }

    /**
     * 获取角色列表
     * @param username 用户名
     * @param isChild 是否仅获取子角色
     * @return 角色列表
     */
    @Override
    public List<Role> getRoleList(String username, Boolean isChild) {
        // 从数据库中查询用户
        User user = userUtil.getUserByUsername(username);

        // 获取所有角色
        List<Role> allRoles = list();
        // 从数据库中查询用户角色
        List<Role> userRoles = userRoleService.getRolesByUserId(user.getId());

        return isChild ? roleUtil.getChildRoleList(userRoles, allRoles) : allRoles;
    }

    /**
     * 获取全局角色列表
     * @return 全局角色列表
     */
    @Override
    public List<Role> getGlobalRoleList() {
        List<Role> allRoles = list();
        return roleUtil.getGlobalChildRoles(allRoles);
    }
}




