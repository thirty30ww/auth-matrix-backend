package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.converter.RoleConverter;
import com.thirty.user.mapper.RoleViewMapper;
import com.thirty.user.model.entity.RoleView;
import com.thirty.user.service.basic.RoleViewService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【role_view(角色页面表)】的数据库操作Service实现
* @createDate 2025-08-22 14:21:16
*/
@Service
public class RoleViewServiceImpl extends ServiceImpl<RoleViewMapper, RoleView>
    implements RoleViewService{

    /**
     * 获取视图id列表
     * @param roleId 角色id
     * @return 视图id列表
     */
    @Override
    public List<Integer> getViewIds(Integer roleId) {
        QueryWrapper<RoleView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        return RoleView.extractViewIds(list(queryWrapper));
    }

    /**
     * 获取视图id列表
     * @param roleIds 角色id列表
     * @return 视图id列表
     */
    @Override
    public List<Integer> getViewIds(List<Integer> roleIds) {
        QueryWrapper<RoleView> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleIds);

        return RoleView.extractViewIds(list(queryWrapper));
    }

    /**
     * 根据角色删除角色视图
     * @param roleId 角色id
     */
    @Override
    public void deleteByRoleId(Integer roleId) {
        QueryWrapper<RoleView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        remove(queryWrapper);
    }

    /**
     * 获取存在的角色视图
     * @param roleIds 角色id列表
     * @param viewIds 视图id列表
     * @return 存在的角色视图列表
     */
    @Override
    public List<String> getExists(List<Integer> roleIds, List<Integer> viewIds) {
        QueryWrapper<RoleView> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleIds).in("view_id", viewIds);
        return RoleView.spliceRoleViewIds(list(queryWrapper));
    }

    /**
     * 添加角色视图
     * @param roleId 角色id
     * @param viewIds 视图id列表
     */
    @Override
    public void addRoleViews(Integer roleId, List<Integer> viewIds) {
        List<RoleView> roleViews = RoleConverter.INSTANCE.toRoleViews(roleId, viewIds);
        saveBatch(roleViews);
    }

    /**
     * 添加角色视图
     * @param roleIds 角色id列表
     * @param viewIds 视图id列表
     */
    @Override
    public void addRoleViews(List<Integer> roleIds, List<Integer> viewIds) {
        if (CollectionUtils.isEmpty(roleIds) || CollectionUtils.isEmpty(viewIds)) {
            return;
        }

        List<RoleView> roleViews = RoleConverter.INSTANCE.toRoleViews(roleIds, viewIds);
        List<String> exists = getExists(roleIds, viewIds);
        roleViews.removeIf(rv -> exists.contains(rv.getRoleId() + "_" + rv.getViewId()));

        saveBatch(roleViews);
    }

    /**
     * 删除角色视图
     * @param roleId 角色id
     * @param viewIds 视图id列表
     */
    @Override
    public void deleteRoleViews(Integer roleId, List<Integer> viewIds) {
        if (roleId == null || CollectionUtils.isEmpty(viewIds)) {
            return;
        }

        QueryWrapper<RoleView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        queryWrapper.in("view_id", viewIds);
        remove(queryWrapper);
    }

    /**
     * 删除角色视图
     * @param roleIds 角色id列表
     * @param viewIds 视图id列表
     */
    @Override
    public void deleteRoleViews(List<Integer> roleIds, List<Integer> viewIds) {
        if (CollectionUtils.isEmpty(roleIds) || CollectionUtils.isEmpty(viewIds)) {
            return;
        }

        QueryWrapper<RoleView> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleIds);
        queryWrapper.in("view_id", viewIds);
        remove(queryWrapper);
    }
}




