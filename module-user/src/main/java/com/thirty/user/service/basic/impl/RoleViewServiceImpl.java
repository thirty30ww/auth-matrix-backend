package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.model.entity.RoleView;
import com.thirty.user.service.basic.RoleViewService;
import com.thirty.user.mapper.RoleViewMapper;
import org.springframework.stereotype.Service;

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
        return list(queryWrapper).stream().map(RoleView::getViewId).toList();
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
        // distinct是java的流式去重
        return list(queryWrapper).stream().map(RoleView::getViewId).distinct().toList();
    }
}




