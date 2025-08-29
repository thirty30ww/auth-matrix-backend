package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.mapper.RoleMapper;
import com.thirty.user.model.entity.Role;
import com.thirty.user.service.basic.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    /**
     * 获取最高级角色
     * @param roleIds 角色id列表
     * @return 最高级角色
     */
    @Override
    public Integer getHighestLevel(List<Integer> roleIds) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", roleIds)
                .ge("level", 0)
                .orderByAsc("level")
                .last("limit 1");
        return getOne(queryWrapper).getLevel();
    }

    /**
     * 获取用户列表
     * @param roleIds 角色id列表
     * @return 用户列表
     */
    @Override
    public List<Role> getRoles(List<Integer> roleIds) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", roleIds);
        return list(queryWrapper);
    }

    /**
     * 添加角色
     * @param role 角色
     */
    @Override
    public void addRole(Role role) {
        Integer level = getLevel(role.getParentNodeId());
        role.setLevel(level);
        save(role);
    }

    /**
     * 更新角色
     * @param role 角色
     */
    @Override
    public void updateRole(Role role) {
        Integer level = getLevel(role.getParentNodeId());
        role.setLevel(level);
        updateById(role);
    }

    /**
     * 获取角色等级
     * @param parentNodeId 父节点id
     * @return 角色等级
     */
    private Integer getLevel(Integer parentNodeId) {
        Role parentRole = getById(parentNodeId);
        return parentRole.getLevel() + 1;
    }
}
